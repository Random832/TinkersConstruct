package slimeknights.tconstruct.library.block;

import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

import static net.minecraft.util.Direction.*;
import static net.minecraft.util.Direction.Axis.*;

public enum EdgeOrientation implements IStringSerializable {
  NORTHWEST(Y, NORTH, WEST),
  NORTHEAST(Y, NORTH, EAST),
  SOUTHWEST(Y, SOUTH, WEST),
  SOUTHEAST(Y, SOUTH, EAST),
  NORTHDOWN(X, NORTH, DOWN),
  SOUTHDOWN(X, SOUTH, DOWN),
  WESTDOWN(Z, WEST, DOWN),
  EASTDOWN(Z, EAST, DOWN),
  NORTHUP(X, NORTH, UP),
  SOUTHUP(X, SOUTH, UP),
  WESTUP(Z, WEST, UP),
  EASTUP(Z, EAST, UP);

  static EdgeOrientation[] BY_YROTATION = {
    NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST,
    NORTHUP, EASTUP, SOUTHUP, WESTUP,
    NORTHDOWN, EASTDOWN, SOUTHDOWN, WESTDOWN,
  };

  // order within each row is quadrant number
  static EdgeOrientation[] BY_AXIS = {
    NORTHDOWN, SOUTHDOWN, NORTHUP, SOUTHUP,  // X
    NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,  // Y
    WESTDOWN, EASTDOWN,  WESTUP, EASTUP, // Z
  };

  static int[] YROTATION_INDICES = new int[12];
  static int[] AXIS_INDICES = new int[12];

  static {
    for (int i = 0; i < 12; i++) {
      YROTATION_INDICES[BY_YROTATION[i].ordinal()] = i;
      AXIS_INDICES[BY_AXIS[i].ordinal()] = i;
    }
  }

  private final Direction face1;
  private final Direction face2;
  private final Direction.Axis axis;

  EdgeOrientation(Direction.Axis axis, Direction face1, Direction face2) {
    this.axis = axis;
    this.face1 = face1;
    this.face2 = face2;
  }

  public boolean isOutsideFace(Direction face) {
     return face == face1 || face == face2;
  }
  private int getYRotationIndex() {
    return YROTATION_INDICES[ordinal()];
  }
  public int getAxisIndex() {
    return AXIS_INDICES[ordinal()];
  }
  public Direction.Axis getAxis() {
    return this.axis;
  }
  public static EdgeOrientation byAxisAndQuadrant(Direction.Axis axis, int quadrant) {
    return BY_AXIS[(axis.ordinal() << 2) | (quadrant^2)];
    // ^2 is because the facing-agnostic quadrant number is based on the
    // bottom/west/south face and this is how it ends up working out.
  }

  public EdgeOrientation rotateY(Rotation rot) {
    int idx = getYRotationIndex();
    int cls = idx & ~3;
    switch (rot) {
      case CLOCKWISE_90:
        return BY_YROTATION[cls | (idx + 1) & 3];
      case CLOCKWISE_180:
        return BY_YROTATION[cls | (idx + 2) & 3];
      case COUNTERCLOCKWISE_90:
        return BY_YROTATION[cls | (idx - 1) & 3];
      default:
        return this;
    }
  }

  public EdgeOrientation mirror(Axis axis) {
    switch (axis) {
      case X:
        switch (this) {
          case NORTHEAST: return NORTHWEST; case NORTHWEST: return NORTHEAST;
          case SOUTHEAST: return SOUTHWEST; case SOUTHWEST: return SOUTHEAST;
          case EASTUP: return WESTUP; case WESTUP: return EASTUP;
          case EASTDOWN: return WESTDOWN; case WESTDOWN: return EASTDOWN;
          default: return this;
        }
      case Y:
        switch(this) {
          case NORTHUP: return NORTHDOWN; case NORTHDOWN: return NORTHUP;
          case SOUTHUP: return SOUTHDOWN; case SOUTHDOWN: return SOUTHUP;
          case EASTUP: return EASTDOWN; case EASTDOWN: return EASTUP;
          case WESTUP: return WESTDOWN; case WESTDOWN: return WESTUP;
          default: return this;
        }
      case Z:
        switch (this) {
          case NORTHEAST: return SOUTHEAST; case SOUTHEAST: return NORTHEAST;
          case NORTHWEST: return SOUTHWEST; case SOUTHWEST: return NORTHWEST;
          case NORTHUP: return SOUTHUP; case SOUTHUP: return NORTHUP;
          case NORTHDOWN: return SOUTHDOWN; case SOUTHDOWN: return NORTHDOWN;
          default: return this;
        }
      default:
        return this;
    }
  }

  @Override
  public String getString() {
    return name().toLowerCase();
  }
}
