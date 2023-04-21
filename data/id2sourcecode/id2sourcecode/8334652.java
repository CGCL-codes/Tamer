    public static void memcpy(CharPtr dst, CharPtr src, int size) {
        for (int i = 0; i < size; i++) {
            dst.write(i, src.read(i));
        }
    }
