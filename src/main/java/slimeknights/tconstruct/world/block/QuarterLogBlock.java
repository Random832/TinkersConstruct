package slimeknights.tconstruct.world.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import slimeknights.tconstruct.library.block.EdgeOrientation;
import slimeknights.tconstruct.library.block.PlacementHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class QuarterLogBlock extends Block {

  public static final Property<EdgeOrientation> ORIENTATION = EnumProperty.create("orientation", EdgeOrientation.class);

  public QuarterLogBlock(Properties properties) {
    super(properties);
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
      EdgeOrientation orientation;
      EdgeOrientation existing = targetState.get(ORIENTATION);
      Direction face = context.getFace();
      if (existing.getAxis() == face.getAxis())
        orientation = existing;
      else if (existing.isOutsideFace(face))
        orientation = null;
      else
        orientation = existing.mirror(face.getAxis());
      if (orientation != null)
        return getDefaultState().with(ORIENTATION, orientation);
    }

    EdgeOrientation orientation = EdgeOrientation.byAxisAndQuadrant(context.getFace().getAxis(), PlacementHelper.getClickedQuadrant(context, false));
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
    switch (mirror) {
      case FRONT_BACK:
        return state.with(ORIENTATION, state.get(ORIENTATION).mirror(Direction.Axis.X));
      case LEFT_RIGHT:
        return state.with(ORIENTATION, state.get(ORIENTATION).mirror(Direction.Axis.Z));
      default:
        return state;
    }
  }
}
