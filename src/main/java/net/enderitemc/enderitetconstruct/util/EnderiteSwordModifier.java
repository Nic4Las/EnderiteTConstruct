package net.enderitemc.enderitetconstruct.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.IncrementalModifier;
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
            // Durability bar under 17 ender pearls
            return 1.0d - getAmount(tool) / 16.0d;
        }
        // Durability bar over 17 ender pearls
        return 1.0d - getAmount(tool) / needed;
    }

    @Override
    public Boolean showDurabilityBar(IModifierToolStack tool, int level) {
        return true;
    }

    @Override
    public int getDurabilityRGB(IModifierToolStack tool, int level) {
        if (getAmount(tool) < 17) {
            // Durability bar under 17 ender pearls
            return 0xe05020;
        }
        // Durability bar over 17 ender pearls
        return 0x0080D0;
    }

    @Override
    public ActionResultType onToolUse(IModifierToolStack tool, int level, World world, PlayerEntity player, Hand hand) {
        // Teleport, onUse and ShiftKeyDown
        if (player.isShiftKeyDown()) {
            Double distance = 30.0d;
            double yaw = (double) player.yHeadRot;
            double pitch = (double) player.xRot;

            // x: 1 = -90, -1 = 90
            // y: 1 = -90, -1 = 90
            // z: 1 = 0, -1 = 180
            double temp = Math.cos(Math.toRadians(pitch));
            double dX = temp * -Math.sin(Math.toRadians(yaw));
            double dY = -Math.sin(Math.toRadians(pitch));
            double dZ = temp * Math.cos(Math.toRadians(yaw));
            Vector3d position = player.position().add(0, player.getEyeHeight() - player.getMyRidingOffset(), 0);
            Vector3d endPosition = new Vector3d(position.x + dX * distance, position.y + dY * distance,
                    position.z + dZ * distance);
            BlockPos blockPos = new BlockPos(endPosition.x, endPosition.y, endPosition.z);

            BlockPos[] blockPoses = { blockPos, blockPos.above(), blockPos };

            double down = endPosition.y;
            double maxDown = down - distance - 1 > 0 ? down - distance - 1 : 0;
            double up = endPosition.y + 1;
            double maxUp = 128;
            // if (player.getCommandSenderWorld().dimensionType().createDragonFight())
            // {
            // maxUp = up + distance - 1 < 127 ? up + distance - 1 : 127;
            // } else {
            maxUp = up + distance - 1 < world.getMaxBuildHeight() ? up + distance - 1 : world.getMaxBuildHeight();
            // }
            double near = distance;

            // Check to Teleport
            if (world.isAreaLoaded(blockPos, 1) && (getAmount(tool) > 0 || player.abilities.instabuild)) {
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
                // world.rayTraceBlock(position, endPosition, blockPos, player.shape,
                // state)

                if (foundSpace > 0 && (position.y + dY * near) < maxUp && (position.y + dY * near) > maxDown) {
                    player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    switch (foundSpace) {
                        case 1: // Down
                            player.teleportToWithTicket(endPosition.x, down > maxDown ? down : maxDown, endPosition.z);
                            break;
                        case 2: // Up
                            player.teleportToWithTicket(endPosition.x, up < maxUp ? up : maxUp, endPosition.z);
                            break;
                        case 4: // Air
                            near = distance / 2;
                        case 3: // Near
                            player.teleportToWithTicket(position.x + dX * near, position.y + dY * near,
                                    position.z + dZ * near);
                            break;
                    }
                    player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 30);
                    if (!player.abilities.instabuild) {
                        setAmount(tool.getPersistentData(), this, getAmount(tool) - 1);
                    }
                    world.broadcastEntityEvent(player, (byte) 46);
                    player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    player.fallDistance = 0;
                } else {
                    return ActionResultType.FAIL;
                }
            } else {
                return ActionResultType.FAIL;
            }
        } else {
            return ActionResultType.FAIL;
        }
        // Sucess only if teleported
        return ActionResultType.SUCCESS;
        // return ActionResult.success(player.getItemInHand(hand));
    }

    protected boolean checkBlocks(World world, BlockPos pos) {
        // Get space of two air blocks with solid ground
        if (world.getBlockState(pos.below()).getMaterial().blocksMotion()
                && !world.getBlockState(pos).getMaterial().blocksMotion()
                && !world.getBlockState(pos.above()).getMaterial().blocksMotion()) {
            return true;
        }
        return false;
    }
}
