package com.builtbroken.cactibucket;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/5/2017.
 */
public class ItemCactiBucket extends Item
{
    @SideOnly(Side.CLIENT)
    private IIcon[] fluid_icons;

    private Fluid[] fluids;

    public ItemCactiBucket()
    {
        setUnlocalizedName("cactibucket:bucket");
        setTextureName("cactibucket:bucket");
        setCreativeTab(CreativeTabs.tabMisc);
    }

    public void postInit()
    {
        fluids = new Fluid[1];
        fluids[0] = FluidRegistry.WATER;
    }

    @Override
    public int getItemStackLimit(ItemStack stack)
    {
        if (getFluid(stack.getItemDamage()) != null)
        {
            return 1;
        }
        return 5;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        Fluid fluid = getFluid(stack.getItemDamage());
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, fluid == null);

        if (movingobjectposition == null)
        {
            return stack;
        }
        else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, i, j, k))
                {
                    return stack;
                }

                if (fluid == null)
                {
                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack))
                    {
                        return stack;
                    }

                    Material material = world.getBlock(i, j, k).getMaterial();
                    int l = world.getBlockMetadata(i, j, k);

                    if (material == Material.water && l == 0)
                    {
                        world.setBlockToAir(i, j, k);
                        return this.placeIntoInventory(stack, player, new ItemStack(this, 1, 1));
                    }
                }
                else
                {
                    if (fluid.getBlock() == null || fluid.getBlock() == Blocks.air)
                    {
                        return new ItemStack(this);
                    }

                    if (movingobjectposition.sideHit == 0)
                    {
                        --j;
                    }

                    if (movingobjectposition.sideHit == 1)
                    {
                        ++j;
                    }

                    if (movingobjectposition.sideHit == 2)
                    {
                        --k;
                    }

                    if (movingobjectposition.sideHit == 3)
                    {
                        ++k;
                    }

                    if (movingobjectposition.sideHit == 4)
                    {
                        --i;
                    }

                    if (movingobjectposition.sideHit == 5)
                    {
                        ++i;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack))
                    {
                        return stack;
                    }

                    if (this.tryPlaceContainedLiquid(world, i, j, k, fluid) && !player.capabilities.isCreativeMode)
                    {
                        return new ItemStack(this);
                    }
                }
            }

            return stack;
        }
    }

    private Fluid getFluid(int meta)
    {
        if (meta >= 1 && meta <= fluids.length)
        {
            return fluids[meta - 1];
        }
        return null;
    }


    private ItemStack placeIntoInventory(ItemStack stack, EntityPlayer player, ItemStack item)
    {
        if (player.capabilities.isCreativeMode)
        {
            return stack;
        }
        else if (--stack.stackSize <= 0)
        {
            return item;
        }
        else
        {
            if (!player.inventory.addItemStackToInventory(item))
            {
                player.dropPlayerItemWithRandomChoice(item, false);
            }

            return stack;
        }
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z, Fluid fluid)
    {
        if (fluid == null || fluid.getBlock() == null || fluid.getBlock() == Blocks.air)
        {
            return false;
        }
        else
        {
            Material material = world.getBlock(x, y, z).getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(x, y, z) && !flag)
            {
                return false;
            }
            else
            {
                if (world.provider.isHellWorld && fluid.getBlock() == Blocks.flowing_water)
                {
                    world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l)
                    {
                        world.spawnParticle("largesmoke", (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                }
                else
                {
                    if (!world.isRemote && flag && !material.isLiquid())
                    {
                        world.func_147480_a(x, y, z, true);
                    }

                    world.setBlock(x, y, z, fluid.getBlock(), 0, 3);
                    fluid.getBlock().onNeighborBlockChange(world, x, y, z, fluid.getBlock());
                }

                return true;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int pass)
    {
        if (pass == 1)
        {
            Fluid fluid = getFluid(meta);
            if (fluid != null && meta >= 1 && meta <= fluid_icons.length)
            {
                IIcon icon = fluid_icons[meta - 1];
                if (icon != null)
                {
                    return icon;
                }
            }
        }
        return this.getIconFromDamage(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.itemIcon = reg.registerIcon(this.getIconString());
        this.fluid_icons = new IIcon[1];
        this.fluid_icons[0] = reg.registerIcon(this.getIconString() + ".water");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getUnlocalizedName(ItemStack stack)
    {
        Fluid fluid = getFluid(stack.getItemDamage());
        if (fluid != null)
        {
            return getUnlocalizedName() + "." + fluid.getName();
        }
        return getUnlocalizedName();
    }
}
