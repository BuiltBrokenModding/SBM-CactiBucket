package com.builtbroken.cactibucket;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.Sound;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/5/2017.
 */
public class ItemCactiBucket extends Item
{
    @SideOnly(Side.CLIENT)

    private Fluid[] fluids;

    public ItemCactiBucket()
    {
        setUnlocalizedName("cactibucket:bucket");
        setRegistryName("cactibucket:bucket");
        setCreativeTab(CreativeTabs.MISC);
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        Fluid fluid = getFluid(stack.getItemDamage());
        RayTraceResult movingobjectposition = this.rayTrace(world, player, fluid == null);

        if (movingobjectposition == null)
        {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }
        else
        {
            if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos pos = movingobjectposition.getBlockPos();
                int i = movingobjectposition.getBlockPos().getX();
                int j = movingobjectposition.getBlockPos().getY();
                int k = movingobjectposition.getBlockPos().getZ();

                if (!world.canMineBlockBody(player, pos))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);

                }

                if (fluid == null)
                {
                    if (!player.canPlayerEdit(pos, movingobjectposition.sideHit, stack))
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
                    }

                    Material material = world.getBlockState(pos).getMaterial();
                    if (material == Material.WATER)
                    {
                        world.setBlockToAir(pos);
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.placeIntoInventory(stack, player, new ItemStack(this, 1, 1)));

                    }
                }
                else
                {
                    if (fluid.getBlock() == null || fluid.getBlock() == Blocks.AIR)
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(this));
                    }

                    if (movingobjectposition.sideHit == EnumFacing.DOWN)
                    {
                        --j;
                    }

                    if (movingobjectposition.sideHit == EnumFacing.UP)
                    {
                        ++j;
                    }

                    if (movingobjectposition.sideHit == EnumFacing.NORTH)
                    {
                        --k;
                    }

                    if (movingobjectposition.sideHit == EnumFacing.SOUTH)
                    {
                        ++k;
                    }

                    if (movingobjectposition.sideHit == EnumFacing.EAST)
                    {
                        --i;
                    }

                    if (movingobjectposition.sideHit == EnumFacing.WEST)
                    {
                        ++i;
                    }

                    if (!player.canPlayerEdit(pos, movingobjectposition.sideHit, stack))
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
                    }

                    if (this.tryPlaceContainedLiquid(world, i, j, k, fluid) && !player.capabilities.isCreativeMode)
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(this));
                    }
                }
            }

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
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
                player.dropItem(item, false);
            }

            return stack;
        }
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World world, int x, int y, int z, Fluid fluid)
    {
        BlockPos pos = new BlockPos(x, y ,z);

        if (fluid == null || fluid.getBlock() == null || fluid.getBlock() == Blocks.AIR)
        {
            return false;
        }
        else
        {
            Material material = world.getBlockState(pos).getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(pos) && !flag)
            {
                return false;
            }
            else
            {
                if (!world.provider.isSurfaceWorld() && fluid.getBlock() == Blocks.FLOWING_WATER)
                {

                    world.playSound((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), SoundEvent.REGISTRY.getObject(new ResourceLocation("random.fizz")), SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F, false);

                    for (int l = 0; l < 8; ++l)
                    {
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                }
                else
                {
                    if (!world.isRemote && flag && !material.isLiquid())
                    {
                        world.destroyBlock(pos, false);
                    }
                    if(!world.isRemote) {
                        Block block = fluid.getBlock();
                        if(block == Blocks.WATER)
                        {
                            block = Blocks.FLOWING_WATER;
                        }
                        world.setBlockState(pos, block.getDefaultState(), 3);
                    }
                }

                return true;
            }
        }
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
