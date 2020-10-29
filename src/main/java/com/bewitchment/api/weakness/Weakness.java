package com.bewitchment.api.weakness;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

public class Weakness {
    private final Set<ResourceLocation> cache = Sets.newHashSet();
    private final Predicate<EntityLivingBase> predicate;

    public static Weakness create(Predicate<EntityLivingBase> predicate, String... ids) {
        return new Weakness(predicate).register(ids);
    }

    private Weakness(Predicate<EntityLivingBase> predicate) {
        this.predicate = predicate;
    }

    /**
     * See {@link Weakness#register(String)}
     */
    public Weakness register(String... ids) {
        Arrays.stream(ids).forEach(this::register);
        return this;
    }

    /**
     * <p> To be used by other mods on {@link FMLPreInitializationEvent}</p>
     * <p> Registerer id into weakness</p>
     * @param id ResourceLocation id. e.g "minecraft:vex"
     * @return Weakness id is being registered to.
     */
    public Weakness register(String id) {
        if (!id.isEmpty()) cache.add(new ResourceLocation(id));
        return this;
    }

    /**
     * @param entity Entity to check/
     * @return True if entity has a weakness.
     */
    public boolean contains(EntityLivingBase entity) {
        return entity != null && get(entity) > 1.0F;
    }

    public float get(EntityLivingBase entity) {
        float weakness = 1.0F;

        if (entity != null && cache.contains(EntityList.getKey(entity)) || predicate.test(entity)) {
            weakness = 1.5F;
            if (entity instanceof EntityPlayer) weakness *= 1.5F;
        }

        return weakness;
    }
}
