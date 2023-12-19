package net.superkat.flutterandflounder.flounderfest;

import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.superkat.flutterandflounder.FlutterAndFlounderMain;
import net.superkat.flutterandflounder.flounderfest.api.FlounderFestApi;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class FlounderFest {
    private final Set<UUID> involvedPlayers = Sets.newHashSet();
    private final int id;
    private final ServerWorld world;
    //The status is always in the player's favor. E.g. "Status.VICTORY" means the player won, not the flounder fest enemies
    private FlounderFest.Status status;


    private final BlockPos startingPos;
    private final int quota;
    public int ticksSinceStart;
    public int ticksSinceEnd;
    public int maxTimeInTicks = 2000;
    private final Set<LivingEntity> enemies = Sets.newHashSet();
    public int enemiesToBeSpawned;
    public int totalEnemyCount;
    public int maxEnemiesAtOnce = 30;
    public int defeatedEnemies = 0;
    public int spawnedEnemies = 0;
    public int currentEnemies = 0;
    public int ticksUntilNextEnemySpawn = 0;
    public int gracePeriod = getGracePeriod();


    public FlounderFest(int id, ServerWorld world, BlockPos startingPos, int quota, int totalEntityCount) {
        this.id = id;
        this.status = FlounderFest.Status.ONGOING;
        this.world = world;
        this.startingPos = startingPos;
        this.quota = quota;
        this.enemiesToBeSpawned = totalEntityCount;
        this.totalEnemyCount = totalEntityCount;
    }

    public ServerPlayerEntity getRandomPlayerTarget() {
        List<ServerPlayerEntity> players = this.world.getPlayers(this.isInFlounderFestDistance());
        return players.get(world.random.nextInt(players.size()));
    }

    private Predicate<ServerPlayerEntity> isInFlounderFestDistance() {
        return player -> {
            BlockPos pos = player.getBlockPos();
            return player.isAlive() && FlounderFestApi.getFlounderFestManager(world).getFlounderFestAt(startingPos, 75) == this;
        };
    }

    public void updateInvolvedPlayers() {
        List<ServerPlayerEntity> players = this.world.getPlayers(this.isInFlounderFestDistance());

        for (ServerPlayerEntity player : players) {
            involvedPlayers.add(player.getUuid());
            if(isGracePeriod()) {
                player.sendMessage(Text.literal("FlounderFest Starting In " + ((gracePeriod) / 20)), true);
            } else {
                player.sendMessage(Text.literal("FlounderFest Time Remaining - " + ((maxTimeInTicks - ticksSinceStart) / 20)), true);
            }
        }
    }

    public void tick() {
        if(isGracePeriod()) {
            gracePeriod--;
        } else {
            ticksSinceStart++;
            ticksUntilNextEnemySpawn--;
        }

        updateInvolvedPlayers();

        if(!this.hasStopped() && !isGracePeriod()) {
            if(this.status == Status.ONGOING) {
                //spawns in enemies every few seconds
                if(currentEnemies < maxEnemiesAtOnce && spawnedEnemies <= totalEnemyCount) {
                    if(ticksUntilNextEnemySpawn <= 0) {
                        addEnemy();
                    }
                }

                //time up
                if(ticksSinceStart >= maxTimeInTicks) {
                    //check for victory
                    if(defeatedEnemies >= quota) {
                        this.status = Status.VICTORY;

                    //counts as loss
                    } else {
                        this.status = Status.LOSS;
                    }
                }
            } else if (isFinished()) {
                ticksSinceEnd++;
                if(ticksSinceStart >= 600) {
                    FlutterAndFlounderMain.LOGGER.info("Flounder Fest " + id + " has finished!");
                    this.invalidate();
                    return;
                }

            }
        }
    }

    public void addEnemy() {
        if(FlounderFestApi.spawnLesserFish(this, this.world, startingPos)) {
            enemiesToBeSpawned--;
            currentEnemies++;
            spawnedEnemies++;
            ticksUntilNextEnemySpawn = world.random.nextBetween(20, 200);
        }
    }

    public void addEntityToEnemyList(LivingEntity entity) {
        List<LivingEntity> set = enemies.stream().toList();
        LivingEntity livingEntity = null;

        for (LivingEntity entityFromSet : set) {
            if(entityFromSet.getUuid().equals(entity.getUuid())) {
                livingEntity = entityFromSet;
                break;
            }
        }

        if(livingEntity != null) {
            enemies.remove(livingEntity);
            enemies.add(entity);
        }

        enemies.add(entity);
    }

    public void updateEnemyCount(boolean didYouDie) {

    }

    public int getGracePeriod() {
        //seconds in ticks
        //grace period is 10 seconds by default
        return 200;
    }

    public boolean isGracePeriod() {
        return gracePeriod > 0;
    }

    public boolean shouldStop() {
        return false;
    }

    public boolean hasStopped() {
        return this.status == Status.STOPPED;
    }

    public boolean isFinished() {
        return this.hasWon() || hasLost();
    }

    public boolean hasWon() {
        return this.status == Status.VICTORY;
    }

    public boolean hasLost() {
        return this.status == Status.LOSS;
    }

    public void invalidate() {
        this.status = Status.STOPPED;

        enemies.forEach(entityFromSet -> (entityFromSet).remove(Entity.RemovalReason.DISCARDED));
    }

    public BlockPos getStartingPos() {
        return startingPos;
    }
    public int getId() {
        return id;
    }

    static enum Status {
        ONGOING,
        VICTORY,
        LOSS,
        STOPPED;

        private static final FlounderFest.Status[] VALUES = values();

        static FlounderFest.Status fromName(String name) {
            for(FlounderFest.Status status : VALUES) {
                if (name.equalsIgnoreCase(status.name())) {
                    return status;
                }
            }

            return ONGOING;
        }

        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
