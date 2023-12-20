package net.superkat.flutterandflounder.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.superkat.flutterandflounder.entity.custom.cod.CodAutomobileEntity;
import net.superkat.flutterandflounder.entity.custom.cod.FlyingCodEntity;
import net.superkat.flutterandflounder.entity.custom.salmon.FlyingSalmonEntity;
import net.superkat.flutterandflounder.entity.custom.salmon.SalmonShipEntity;

import static net.superkat.flutterandflounder.FlutterAndFlounderMain.MOD_ID;

public class FlutterAndFlounderEntities {
    public static final EntityType<FlyingCodEntity> FLYING_COD = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "flyingcod"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FlyingCodEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.5f)).build()
    );

    public static final EntityType<FlyingSalmonEntity> FLYING_SALMON = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "flyingsalmon"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FlyingSalmonEntity::new)
                    .dimensions(EntityDimensions.fixed(1.3f, 0.6f)).build()
    );

    //bosses

    public static final EntityType<CodAutomobileEntity> COD_AUTOMOBILE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "codautomobile"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, CodAutomobileEntity::new)
                    .dimensions(EntityDimensions.fixed(1.2f, 0.8f)).build()
    );

    public static final EntityType<SalmonShipEntity> SALMON_SHIP = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "salmonship"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SalmonShipEntity::new)
                    .dimensions(EntityDimensions.fixed(1.5f, 1f)).build()
    );

}
