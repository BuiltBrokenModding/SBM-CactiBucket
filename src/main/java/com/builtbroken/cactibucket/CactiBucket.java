package com.builtbroken.cactibucket;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/5/2017.
 */
@Mod(modid = CactiBucket.MOD_ID, name = "[SBM] Cacti Bucket", version = CactiBucket.VERSION)
@Mod.EventBusSubscriber()
public class CactiBucket
{
    public static final String MAJOR_VERSION = "1";
    public static final String MINOR_VERSION = "1";
    public static final String REVISION_VERSION = "0";
    public static final String BUILD_VERSION = "1";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;

    public static final String MOD_ID = "cactibucket";

    public static ItemCactiBucket itemBucket = new ItemCactiBucket();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(itemBucket);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        itemBucket.postInit();
        GameRegistry.addShapedRecipe(new ItemStack(itemBucket), "SSS", "L L", "LLL", 'L', Blocks.CACTUS, 'S', Items.STICK);
    }

}
