package io.github.fallOut015.bucket_of_chicken.world.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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
            if(!BucketOfChickenItem.newChicken(useOnContext.getItemInHand())) {
                chicken.deserializeNBT(BucketOfChickenItem.getNBTData(useOnContext.getItemInHand()));
            }
            serverLevel.addFreshEntity(chicken);
            useOnContext.getPlayer().playSound(SoundEvents.CHICKEN_HURT, 1.0f, 1.0f);

            if(!useOnContext.getPlayer().isCreative()) {
                useOnContext.getPlayer().setItemInHand(useOnContext.getHand(), new ItemStack(Items.BUCKET));
            }
        }
        return InteractionResult.sidedSuccess(useOnContext.getLevel() instanceof ServerLevel);
    }
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        if(level.isClientSide && entity instanceof Player player) {
            int n = 0;
            for(ItemStack chicken : player.getAllSlots()) {
                if(chicken.is(this)) {
                    ++ n;
                }
            }
            // find which index of all chickens itemStack is
            if((entity.tickCount + 12) % Math.floor(12f / (float) n) == 0) {
                boolean isBaby = getNBTData(itemStack).getInt("Age") < 0;
                float pitch = isBaby ? (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.5F : (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.0F;
                float volume = 0.1f;

                if(ItemStack.matches(itemStack, player.getItemBySlot(EquipmentSlot.MAINHAND)) || ItemStack.matches(itemStack, player.getItemBySlot(EquipmentSlot.OFFHAND))) {
                    volume = 1.0f;
                } else {
                    int slot = -1;
                    for(int j = 0; j < 9; ++ j) {
                        if(ItemStack.matches(itemStack, player.getSlot(j).get())) {
                            slot = j;
                        }
                    }

                    if(Minecraft.getInstance().screen == null) {
                        if(slot != -1) {
                            int distance = Math.abs(player.getInventory().selected - slot);
                            volume = 1f / ((float) Math.abs(distance) + 1f);
                        }
                    } else if(Minecraft.getInstance().screen instanceof InventoryScreen inventoryScreen) {
                        int x = (int) (Minecraft.getInstance().mouseHandler.xpos() * (double) Minecraft.getInstance().getWindow().getGuiScaledWidth() / (double) Minecraft.getInstance().getWindow().getScreenWidth());
                        int y = (int) (Minecraft.getInstance().mouseHandler.ypos() * (double) Minecraft.getInstance().getWindow().getGuiScaledHeight() / (double) Minecraft.getInstance().getWindow().getScreenHeight());

                        int xd = inventoryScreen.getMenu().slots.get(slot).x - x;
                        int yd = inventoryScreen.getMenu().slots.get(slot).y - y;

                        float d = (float) Math.sqrt(Math.pow(xd, 2) + Math.pow(yd, 2));

                        volume = Math.max(0, -d + 256);
                    }
                }

                entity.playSound(SoundEvents.CHICKEN_HURT, volume, pitch);
            }
        }
    }

    @Override
    public Component getName(ItemStack itemStack) {
        if(!BucketOfChickenItem.newChicken(itemStack)) {
            if(BucketOfChickenItem.getNBTData(itemStack).contains("CustomName")) {
                String s = BucketOfChickenItem.getNBTData(itemStack).getString("CustomName");
                Component customName = Component.Serializer.fromJson(s);
                if(customName != null) {
                    return new TranslatableComponent("item.bucket_of_chicken.bucket_of_chicken_with_name").append(customName);
                }
            }
        }
        return super.getName(itemStack);
    }

    public static void setNBTData(ItemStack itemStack, Entity entity) {
        itemStack.getOrCreateTag().put("data", entity.serializeNBT());
    }
    public static CompoundTag getNBTData(ItemStack itemStack) {
        return itemStack.getOrCreateTag().contains("data") ? (CompoundTag) itemStack.getTag().get("data") : new CompoundTag();
    }
    public static boolean newChicken(ItemStack itemStack) {
        return !itemStack.getOrCreateTag().contains("data");
    }
}