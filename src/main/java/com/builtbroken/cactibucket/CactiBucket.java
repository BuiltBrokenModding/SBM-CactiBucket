package com.builtbroken.cactibucket;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/5/2017.
 */
@Mod(modid = "cactibucket", name = "Cacti Bucket", version = CactiBucket.VERSION)
public class CactiBucket
{
    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;

    public static ItemCactiBucket itemBucket;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        itemBucket = new ItemCactiBucket();
        GameRegistry.registerItem(itemBucket, "cbCactiBucket");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        itemBucket.postInit();
        GameRegistry.addShapedRecipe(new ItemStack(itemBucket), "SSS", "L L", "LLL", 'L', Blocks.cactus, 'S', Items.stick);
    }
}
