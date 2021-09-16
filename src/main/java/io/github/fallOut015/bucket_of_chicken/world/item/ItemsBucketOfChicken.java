package io.github.fallOut015.bucket_of_chicken.world.item;

import io.github.fallOut015.bucket_of_chicken.MainBucketOfChicken;
import io.github.fallOut015.bucket_of_chicken.world.item.food.FoodsBucketOfChicken;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsBucketOfChicken {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MainBucketOfChicken.MODID);

    public static final RegistryObject<Item> BUCKET_OF_CHICKEN = ITEMS.register("bucket_of_chicken", () -> new BucketOfChickenItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> BUCKET_OF_FRIED_CHICKEN = ITEMS.register("bucket_of_fried_chicken", () -> new BucketOfFriedChickenItem(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).craftRemainder(Items.BUCKET).stacksTo(1).food(FoodsBucketOfChicken.BUCKET_OF_FRIED_CHICKEN)));
    
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}