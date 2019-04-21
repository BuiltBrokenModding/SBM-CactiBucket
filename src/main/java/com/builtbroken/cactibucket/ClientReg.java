package com.builtbroken.cactibucket;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Dark(DarkGuardsman, Robert) on 4/21/2019.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientReg
{
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(CactiBucket.itemBucket, 0, new ModelResourceLocation(CactiBucket.itemBucket.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(CactiBucket.itemBucket, 1, new ModelResourceLocation(CactiBucket.itemBucket.getRegistryName() + ".water", "inventory"));
    }
}
