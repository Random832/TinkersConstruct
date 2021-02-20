package slimeknights.tconstruct.library.block;

import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MathHelper;

public enum EdgeOrientation implements IStringSerializable {
    // there's no good order for this, so try
    // to avoid using ordinal externally
    NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,
    NORTHDOWN, SOUTHDOWN, WESTDOWN, EASTDOWN,
    NORTHUP, SOUTHUP, WESTUP, EASTUP;

    static EdgeOrientation[] BY_YROTATION = {
            NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST,
            NORTHUP, EASTUP, SOUTHUP, WESTUP,
            NORTHDOWN, EASTDOWN, SOUTHDOWN, WESTDOWN,
    };

    // may be useful for blockstate gen if we want to support four-part heartwood textures
    // the index within each class is a clockwise rotation order along the axis
    static EdgeOrientation[] BY_AXIS = {
            NORTHDOWN, SOUTHDOWN, SOUTHUP, NORTHUP,   // X
            NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST, // Y
            WESTDOWN, EASTDOWN, EASTUP, WESTUP,  // Z
    };

    static int[] YROTATION_INDICES = new int[12];
    static int[] AXIS_INDICES = new int[12];
    static {
        for(int i=0; i<12; i++) {
            YROTATION_INDICES[BY_YROTATION[i].ordinal()] = i;
            AXIS_INDICES[BY_AXIS[i].ordinal()] = i;
        }
    }

    private int getYRotationIndex() {
        return YROTATION_INDICES[ordinal()];
    }
    private EdgeOrientation ByYRotationIndex(int i) {
        return BY_YROTATION[i];
    }
    public int getAxisIndex() {
        return AXIS_INDICES[ordinal()];
    }
    public EdgeOrientation byAxisIndex(int i) {
        return BY_AXIS[i];
    }

    private static EdgeOrientation[] BY_ANGLE = new EdgeOrientation[]{SOUTHEAST, SOUTHWEST, NORTHWEST, NORTHEAST};
    public static EdgeOrientation fromAngle(float angle) {
        return BY_ANGLE[(MathHelper.floor((angle + 45) / 90.0D + 0.5D)) & 3];
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

    public EdgeOrientation mirror(Mirror mirror) {
        switch (mirror) {
            case FRONT_BACK: // east west
                switch (this) {
                    case NORTHEAST:
                        return NORTHWEST;
                    case NORTHWEST:
                        return NORTHEAST;
                    case SOUTHEAST:
                        return SOUTHWEST;
                    case SOUTHWEST:
                        return SOUTHEAST;
                    case EASTUP:
                        return WESTUP;
                    case WESTUP:
                        return EASTUP;
                    case EASTDOWN:
                        return WESTDOWN;
                    case WESTDOWN:
                        return EASTDOWN;
                    default:
                        return this;
                }
            case LEFT_RIGHT: // north south
                switch (this) {
                    case NORTHEAST:
                        return NORTHWEST;
                    case NORTHWEST:
                        return NORTHEAST;
                    case SOUTHEAST:
                        return SOUTHWEST;
                    case SOUTHWEST:
                        return SOUTHEAST;
                    case NORTHUP:
                        return SOUTHUP;
                    case NORTHDOWN:
                        return SOUTHDOWN;
                    case SOUTHUP:
                        return NORTHUP;
                    case SOUTHDOWN:
                        return NORTHDOWN;
                    default:
                        return this;
                }
            default:
                return this;
        }
    }

    @Override
    public String getString() {
        return name().toLowerCase();
    }

    public Direction.Axis getAxis() {
        switch(getAxisIndex() / 4) {
            case 0:
                return Direction.Axis.X;
            case 1:
            default:
                return Direction.Axis.Y;
            case 2:
                return Direction.Axis.Z;
        }
    }
}
