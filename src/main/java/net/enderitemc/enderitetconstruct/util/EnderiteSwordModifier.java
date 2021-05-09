package net.enderitemc.enderitetconstruct.util;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardCharTypedEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.recipe.tinkerstation.modifier.ModifierRecipeLookup;
import slimeknights.tconstruct.library.tools.ToolDefinition;
import slimeknights.tconstruct.library.tools.nbt.IModDataReadOnly;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;

public class EnderiteSwordModifier extends IncrementalModifier {
    public static final ResourceLocation ENDERITE_SWORD = Util.getResource("enderite_sword");
    // public static final ResourceLocation ENDERITE_CHARGE =
    // Util.getResource("enderite_charge");

    public EnderiteSwordModifier() {
        super(0x306850);
    }

    @Override
    public void addVolatileData(ToolDefinition toolDefinition, StatsNBT baseStats, IModDataReadOnly persistentData,
            int level, ModDataNBT volatileData) {
        volatileData.putBoolean(ENDERITE_SWORD, true);
    }

    @Override
    public ITextComponent getDisplayName(IModifierToolStack tool, int level) {
        int neededPerLevel = ModifierRecipeLookup.getNeededPerLevel(this);
        ITextComponent name = this.getDisplayName(level);
        int amount = getAmount(tool);
        return name.copy().append(": " + amount + " / " + neededPerLevel);
    }

    @Override
    public ITextComponent getDisplayName(int level) {
        return getDisplayName();
    }

    @Override
    public double getDamagePercentage(IModifierToolStack tool, int level) {
        double needed = ModifierRecipeLookup.getNeededPerLevel(this) * 1.0d;
        if (getAmount(tool) < 17) {
            return 1.0d - getAmount(tool) / 16.0d;
        }
        return 1.0d - getAmount(tool) / needed;
    }

    @Override
    public Boolean showDurabilityBar(IModifierToolStack tool, int level) {
        return true;
    }

    @Override
    public int getDurabilityRGB(IModifierToolStack tool, int level) {
        if (getAmount(tool) < 17) {
            return 0xcf0000;
        }
        return 0x0080D0;
    }

    @Override
    public void onInventoryTick(IModifierToolStack tool, int level, World world, LivingEntity holder, int itemSlot,
            boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isSelected && isCorrectSlot && Minecraft.getInstance().options.keyShift.isDown()
                && Minecraft.getInstance().options.keyUse.isDown()
                && !((PlayerEntity) holder).getCooldowns().isOnCooldown(stack.getItem())) {
            Double distance = 30.0d;
            double yaw = (double) holder.yHeadRot;
            double pitch = (double) holder.xRot;

            // x: 1 = -90, -1 = 90
            // y: 1 = -90, -1 = 90
            // z: 1 = 0, -1 = 180
            double temp = Math.cos(Math.toRadians(pitch));
            double dX = temp * -Math.sin(Math.toRadians(yaw));
            double dY = -Math.sin(Math.toRadians(pitch));
            double dZ = temp * Math.cos(Math.toRadians(yaw));
            Vector3d position = holder.position().add(0, holder.getEyeHeight() - holder.getMyRidingOffset(), 0);
            Vector3d endPosition = new Vector3d(position.x + dX * distance, position.y + dY * distance,
                    position.z + dZ * distance);
            BlockPos blockPos = new BlockPos(endPosition.x, endPosition.y, endPosition.z);

            BlockPos[] blockPoses = { blockPos, blockPos.above(), blockPos };

            double down = endPosition.y;
            double maxDown = down - distance - 1 > 0 ? down - distance - 1 : 0;
            double up = endPosition.y + 1;
            double maxUp = 128;
            // if (holder.getCommandSenderWorld().dimensionType().createDragonFight())
            // {
            // maxUp = up + distance - 1 < 127 ? up + distance - 1 : 127;
            // } else {
            maxUp = up + distance - 1 < 255 ? up + distance - 1 : 255;
            // }
            double near = distance;

            // Check to Teleport
            if (world.isAreaLoaded(blockPos, 1)
                    && (getAmount(tool) > 0 || ((PlayerEntity) holder).abilities.instabuild)) {
                int foundSpace = 0;

                while (foundSpace == 0 && (blockPoses[0].getY() > maxDown || blockPoses[1].getY() < maxUp)) {
                    // CheckDown
                    if (blockPoses[0].getY() > maxDown) {
                        if (checkBlocks(world, blockPoses[0])) {
                            foundSpace = 1;
                        } else {
                            --down;
                            blockPoses[0] = blockPoses[0].below();
                        }
                    }
                    // CheckUp
                    if (blockPoses[1].getY() < maxUp) {
                        if (checkBlocks(world, blockPoses[1])) {
                            foundSpace = 2;
                        } else {
                            ++up;
                            blockPoses[1] = blockPoses[1].above();
                        }
                    }
                    // CheckNear
                    if (near > 2) {
                        if (checkBlocks(world, blockPoses[2])) {
                            foundSpace = 3;
                        } else {
                            --near;
                            blockPoses[2] = blockPoses[2].offset(-dX, -dY, -dZ);
                        }
                    }
                }
                if (foundSpace == 0 && !world.getBlockState(blockPos).getMaterial().blocksMotion()
                        && !world.getBlockState(blockPos.above()).getMaterial().blocksMotion()) {
                    foundSpace = 4;
                }
                // world.rayTraceBlock(position, endPosition, blockPos, holder.shape,
                // state)

                // System.out.printf("| %s, %s, %s, %s |\r\n", foundSpace, down, up, near);

                if (foundSpace > 0 && (position.y + dY * near) < maxUp && (position.y + dY * near) > maxDown) {
                    holder.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    switch (foundSpace) {
                        case 1: // Down
                            holder.teleportToWithTicket(endPosition.x, down > maxDown ? down : maxDown, endPosition.z);
                            break;
                        case 2: // Up
                            holder.teleportToWithTicket(endPosition.x, up < maxUp ? up : maxUp, endPosition.z);
                            break;
                        case 4: // Air
                            near = distance / 2;
                        case 3: // Near
                            holder.teleportToWithTicket(position.x + dX * near, position.y + dY * near,
                                    position.z + dZ * near);
                            break;
                    }
                    ((PlayerEntity) holder).getCooldowns().addCooldown(stack.getItem(), 30);
                    // if (!holder.abilities.creativeMode) {
                    // holder.inventory.getStack(slot).decrement(1);
                    // }
                    if (!((PlayerEntity) holder).abilities.instabuild) {
                        setAmount(tool.getPersistentData(), this, getAmount(tool) - 1);
                    }
                    world.broadcastEntityEvent(holder, (byte) 46);
                    holder.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }

        }

    }

    protected boolean checkBlocks(World world, BlockPos pos) {
        if (world.getBlockState(pos.below()).getMaterial().blocksMotion()
                && !world.getBlockState(pos).getMaterial().blocksMotion()
                && !world.getBlockState(pos.above()).getMaterial().blocksMotion()) {
            return true;
        }
        return false;
    }
}
