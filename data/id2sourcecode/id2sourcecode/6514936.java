    public static void rotateArray(Object[] array) {
        Object top = array[0];
        for (int i = 0, len = array.length - 1; i < len; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = top;
    }
