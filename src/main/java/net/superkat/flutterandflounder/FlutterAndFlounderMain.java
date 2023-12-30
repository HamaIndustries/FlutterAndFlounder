package net.superkat.flutterandflounder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.GameRules;
import net.superkat.flutterandflounder.entity.FlutterAndFlounderEntities;
import net.superkat.flutterandflounder.entity.custom.cod.*;
import net.superkat.flutterandflounder.entity.custom.frogmobile.FrogmobileEntity;
import net.superkat.flutterandflounder.entity.custom.salmon.FlyingSalmonEntity;
import net.superkat.flutterandflounder.entity.custom.salmon.SalmonShipEntity;
import net.superkat.flutterandflounder.entity.custom.salmon.SalmonSniperEntity;
import net.superkat.flutterandflounder.entity.custom.salmon.WhackerSalmonEntity;
import net.superkat.flutterandflounder.flounderfest.command.FlounderFestCommand;
import net.superkat.flutterandflounder.item.FlutterAndFlounderItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

public class FlutterAndFlounderMain implements ModInitializer {
	public static final String MOD_ID = "flutterandflounder";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final GameRules.Key<GameRules.BooleanRule> END_FLOUNDERFEST_UPON_ALL_PLAYERS_DEAD =
			GameRuleRegistry.register("endFlounderFestWhenAllPlayersAreDead", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

	@Override
	public void onInitialize() {

		GeckoLib.initialize();

		FlutterAndFlounderItems.init();

		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.FLYING_COD, FlyingCodEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.FLYING_SALMON, FlyingSalmonEntity.createAttributes());

		//bosses
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.COD_AUTOMOBILE, CodAutomobileEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.SALMON_SHIP, SalmonShipEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.HAMMER_COD, HammerCodEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.WHACKER_SALMON, WhackerSalmonEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.CHILL_COD, ChillCodEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.SALMON_SNIPER, SalmonSniperEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.CLOWN_COD, ClownCodEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.GOON, GoonCodEntity.createAttributes());

		//frogmobile
		FabricDefaultAttributeRegistry.register(FlutterAndFlounderEntities.FROGMOBILE, FrogmobileEntity.createAttributes());


		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> FlounderFestCommand.register(dispatcher)));

		LOGGER.info("Hello Fabric world!");
	}
}