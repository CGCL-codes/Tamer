    @SuppressWarnings("unchecked")
    public static void transpose21(Object a, int ia, int n1, int n2) {
        int move_size = (n1 + n2) / 2;
        byte[] move = new byte[move_size];
        Class componentType = a.getClass().getComponentType();
        if (componentType == null) throw new IllegalArgumentException("Input argument must be an array of objects or primitive types");
        if (componentType.equals(byte.class)) {
            byte21a((byte[]) a, ia, n1, n2, move, move_size);
        } else if (componentType.equals(short.class)) {
            short21a((short[]) a, ia, n1, n2, move, move_size);
        } else if (componentType.equals(int.class)) {
            int21a((int[]) a, ia, n1, n2, move, move_size);
        } else if (componentType.equals(float.class)) {
            float21a((float[]) a, ia, n1, n2, move, move_size);
        } else if (componentType.equals(long.class)) {
            long21a((long[]) a, ia, n1, n2, move, move_size);
        } else if (componentType.equals(double.class)) {
            double21a((double[]) a, ia, n1, n2, move, move_size);
        } else {
            object21a((Object[]) a, ia, n1, n2, move, move_size);
        }
    }
