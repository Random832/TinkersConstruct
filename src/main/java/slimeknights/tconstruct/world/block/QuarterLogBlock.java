package slimeknights.tconstruct.world.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import slimeknights.tconstruct.library.block.EdgeOrientation;
import slimeknights.tconstruct.library.block.PlacementHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class QuarterLogBlock extends net.minecraft.block.Block {

  public static final net.minecraft.state.Property<EdgeOrientation> ORIENTATION = EnumProperty.create("orientation", EdgeOrientation.class);

  public QuarterLogBlock(AbstractBlock.Properties properties) {
    super(properties);
  }

  @Nullable
  public static EdgeOrientation getPlacementForExistingLog(EdgeOrientation existing, Direction face) {
    if (existing.getAxis() == face.getAxis())
      return existing;
    switch (face) {
      case DOWN:
        switch (existing) {
          case EASTUP:
            return EdgeOrientation.EASTDOWN;
          case NORTHUP:
            return EdgeOrientation.NORTHDOWN;
          case SOUTHUP:
            return EdgeOrientation.SOUTHDOWN;
          case WESTUP:
            return EdgeOrientation.WESTDOWN;
        }
        break;
      case UP:
        switch (existing) {
          case EASTDOWN:
            return EdgeOrientation.EASTUP;
          case NORTHDOWN:
            return EdgeOrientation.NORTHUP;
          case SOUTHDOWN:
            return EdgeOrientation.SOUTHUP;
          case WESTDOWN:
            return EdgeOrientation.WESTUP;
        }
        break;
      case NORTH:
        switch (existing) {
          case SOUTHDOWN:
            return EdgeOrientation.NORTHDOWN;
          case SOUTHEAST:
            return EdgeOrientation.NORTHEAST;
          case SOUTHUP:
            return EdgeOrientation.NORTHUP;
          case SOUTHWEST:
            return EdgeOrientation.NORTHWEST;
        }
        break;
      case SOUTH:
        switch (existing) {
          case NORTHDOWN:
            return EdgeOrientation.SOUTHDOWN;
          case NORTHEAST:
            return EdgeOrientation.SOUTHEAST;
          case NORTHUP:
            return EdgeOrientation.SOUTHUP;
          case NORTHWEST:
            return EdgeOrientation.SOUTHWEST;
        }
        break;
      case EAST:
        switch (existing) {
          case NORTHWEST:
            return EdgeOrientation.NORTHEAST;
          case SOUTHWEST:
            return EdgeOrientation.SOUTHEAST;
          case WESTDOWN:
            return EdgeOrientation.EASTDOWN;
          case WESTUP:
            return EdgeOrientation.EASTUP;
        }
        break;
      case WEST:
        switch (existing) {
          case EASTDOWN:
            return EdgeOrientation.WESTDOWN;
          case EASTUP:
            return EdgeOrientation.WESTUP;
          case NORTHEAST:
            return EdgeOrientation.NORTHWEST;
          case SOUTHEAST:
            return EdgeOrientation.SOUTHWEST;
        }
        break;
    }
    return null;
  }

  private EdgeOrientation getPlacementForOther(int octant, Direction face) {
    EdgeOrientation orientation;
    switch (face.getAxis()) {
      case X:
        switch (octant) {
          case 0:
          case 1:
            return EdgeOrientation.SOUTHUP;
          case 2:
          case 3:
            return EdgeOrientation.NORTHUP;
          case 4:
          case 5:
            return EdgeOrientation.NORTHDOWN;
          case 6:
          case 7:
            return EdgeOrientation.SOUTHDOWN;
        }
      case Y:
        switch (octant) {
          case 0:
          case 1:
            return EdgeOrientation.SOUTHEAST;
          case 2:
          case 3:
            return EdgeOrientation.SOUTHWEST;
          case 4:
          case 5:
            return EdgeOrientation.NORTHWEST;
          case 6:
          case 7:
            return EdgeOrientation.NORTHEAST;
        }
      case Z:
        switch (octant) {
          case 0:
          case 1:
            return EdgeOrientation.EASTUP;
          case 2:
          case 3:
            return EdgeOrientation.WESTUP;
          case 4:
          case 5:
            return EdgeOrientation.WESTDOWN;
          case 6:
          case 7:
            return EdgeOrientation.EASTDOWN;
        }
    }
    return EdgeOrientation.SOUTHEAST;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(ORIENTATION);
  }


  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    BlockState targetState = PlacementHelper.getClickedBlockState(context);

    if (targetState.getBlock() instanceof QuarterLogBlock && !context.hasSecondaryUseForPlayer()) {
      EdgeOrientation orientation = getPlacementForExistingLog(targetState.get(ORIENTATION), context.getFace());
      if (orientation != null)
        return getDefaultState().with(ORIENTATION, orientation);
    }

    EdgeOrientation orientation = getPlacementForOther(PlacementHelper.getClickedFaceOctant(context, false), context.getFace());
    // orientation = EdgeOrientation.fromAngle(player.rotationYaw + 180);
    return this.getDefaultState().with(ORIENTATION, orientation);
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState rotate(BlockState state, Rotation rot) {
    return state.with(ORIENTATION, state.get(ORIENTATION).rotateY(rot));
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.with(ORIENTATION, state.get(ORIENTATION).mirror(mirror));
  }
}
