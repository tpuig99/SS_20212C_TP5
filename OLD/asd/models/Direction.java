package models;

public enum Direction {
    L,
    R,
    T,
    B,
    M,
    V;

    public static boolean isValue(String item) {
        for (Direction direction : Direction.values()) {
            if (direction.name().equals(item)) {
                return true;
            }
        }
        return false;
    }
}
