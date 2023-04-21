    @SuppressWarnings("unchecked")
    public static void transpose132(Object a, int ia, int n1, int n2, int n3) {
        int move_size = (n2 + n3) / 2;
        byte[] move = new byte[move_size];
        Class componentType = a.getClass().getComponentType();
        if (componentType == null) throw new IllegalArgumentException("Input argument must be an array of objects or primitive types");
        if (componentType.equals(byte.class)) {
            byte132a((byte[]) a, ia, n1, n2, n3, move, move_size);
        } else if (componentType.equals(short.class)) {
            short132a((short[]) a, ia, n1, n2, n3, move, move_size);
        } else if (componentType.equals(int.class)) {
            int132a((int[]) a, ia, n1, n2, n3, move, move_size);
        } else if (componentType.equals(float.class)) {
            float132a((float[]) a, ia, n1, n2, n3, move, move_size);
        } else if (componentType.equals(long.class)) {
            long132a((long[]) a, ia, n1, n2, n3, move, move_size);
        } else if (componentType.equals(double.class)) {
            double132a((double[]) a, ia, n1, n2, n3, move, move_size);
        } else {
            object132a((Object[]) a, ia, n1, n2, n3, move, move_size);
        }
    }
