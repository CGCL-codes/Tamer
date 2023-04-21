    private static final void arrayCopy(Object src, int srcPos, int[] dst, int dstPos, int length) {
        if (src.getClass().isArray()) {
            if (src.getClass().getComponentType() == short.class) {
                short[] srcArray = (short[]) src;
                for (int i = 0; i < length; i++) {
                    dst[dstPos + i] = srcArray[srcPos + i];
                }
            } else if (src.getClass().getComponentType() == byte.class) {
                byte[] srcArray = (byte[]) src;
                for (int i = 0; i < length; i++) {
                    dst[dstPos + i] = srcArray[srcPos + i];
                }
            }
        }
    }
