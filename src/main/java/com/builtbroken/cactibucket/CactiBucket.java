package com.builtbroken.cactibucket;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/5/2017.
 */
@Mod(modid = "cactibucket", name = "Cacti Bucket", version = CactiBucket.VERSION)
@Mod.EventBusSubscriber()
public class CactiBucket
{
    public static final String MAJOR_VERSION = "1";
    public static final String MINOR_VERSION = "1";
    public static final String REVISION_VERSION = "0";
    public static final String BUILD_VERSION = "1";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;

    public static ItemCactiBucket itemBucket = new ItemCactiBucket();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(itemBucket);
    }
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(itemBucket, 0, new ModelResourceLocation(itemBucket.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(itemBucket, 1, new ModelResourceLocation(itemBucket.getRegistryName() + ".water", "inventory"));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        itemBucket.postInit();
        GameRegistry.addShapedRecipe(new ItemStack(itemBucket), "SSS", "L L", "LLL", 'L', Blocks.CACTUS, 'S', Items.STICK);
    }

}
