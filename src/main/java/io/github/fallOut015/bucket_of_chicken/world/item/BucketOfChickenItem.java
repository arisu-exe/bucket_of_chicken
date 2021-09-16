package io.github.fallOut015.bucket_of_chicken.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;

public class BucketOfChickenItem extends Item {
    public BucketOfChickenItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if(useOnContext.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos blockpos = useOnContext.getClickedPos();

            if (!serverLevel.getBlockState(blockpos).getCollisionShape(serverLevel, blockpos).isEmpty()) {
                blockpos = blockpos.relative(useOnContext.getClickedFace());
            }

            Entity chicken = new Chicken(EntityType.CHICKEN, serverLevel);
            chicken.setPos(blockpos.getX() + 0.5d, blockpos.getY(), blockpos.getZ() + 0.5d);
            serverLevel.addFreshEntity(chicken);

            if(!useOnContext.getPlayer().isCreative()) {
                useOnContext.getPlayer().setItemInHand(useOnContext.getHand(), new ItemStack(Items.BUCKET));
            }
        }
        return InteractionResult.sidedSuccess(useOnContext.getLevel() instanceof ServerLevel);
    }
}