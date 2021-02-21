package slimeknights.tconstruct.library.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class PlacementHelper {
  public static BlockState getClickedBlockState(ItemUseContext context) {
    return context.getWorld().getBlockState(getActualPos(context));
  }

  public static BlockState getClickedBlockState(BlockItemUseContext context) {
    return context.getWorld().getBlockState(getActualPos(context));
  }

  private static BlockPos getActualPos(ItemUseContext context) {
    if (context instanceof BlockItemUseContext)
      return getActualPos((BlockItemUseContext) context);
    else
      return context.getPos();
  }

  private static BlockPos getActualPos(BlockItemUseContext context) {
    Direction face = context.getFace();
    BlockPos pos = context.getPos();
    return context.replacingClickedOnBlock() ? pos : pos.offset(face, -1);
  }

  public static class Vector2d {
    public final double x;
    public final double y;

    public Vector2d(double x, double y) {
      this.x = x;
      this.y = y;
    }
  }

  /**
   * @param context context object of click
   * @param doFlip flip coordinates on north/east/top to match default texture mapping
   * @return 0..4 numbered from top left of texture
   */
  public static int getClickedQuadrant(ItemUseContext context, boolean doFlip) {
    Vector2d vec = getClickedFacePos(context, doFlip);
    int a = vec.x >= 0 ? 1 : 0;
    int b = vec.y < 0 ? 2 : 0;
    return a + b;
  }

  /**
   * @param context context object of click
   * @param doFlip flip coordinates on north/east/top to match default texture mapping
   * @return pair of coordinates from -0.5 to 0.5
   */
  public static Vector2d getClickedFacePos(ItemUseContext context, boolean doFlip) {
    // offset of actualPos doesn't matter here because we are only considering the other two axes
    Vector3d relativeHitVec = context.getHitVec().subtract(Vector3d.copyCentered(context.getPos()));
    double x, y;
    int flip;
    if(doFlip) {
      switch(context.getFace()) {
        case NORTH:
        case EAST:
        case UP:
          flip = -1;
          break;
        default:
          flip = 1;
          break;
      }
    } else {
      flip = 1;
    }
    switch (context.getFace().getAxis()) {
      case X:
        x = relativeHitVec.z * flip;
        y = relativeHitVec.y;
        break;
      case Y:
        x = relativeHitVec.x;
        y = relativeHitVec.z * flip;
        break;
      case Z:
      default:
        x = relativeHitVec.x * flip;
        y = relativeHitVec.y;
        break;
    }
    return new Vector2d(x, y);
  }
}
