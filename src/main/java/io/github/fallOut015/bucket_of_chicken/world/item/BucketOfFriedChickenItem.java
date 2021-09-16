package io.github.fallOut015.bucket_of_chicken.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BucketOfFriedChickenItem extends Item {
    public BucketOfFriedChickenItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        return new ItemStack(livingEntity instanceof Player && ((Player) livingEntity).isCreative() ? this : Items.BUCKET);
    }
}