    private static void shiftleft(int array[], int size) {
        if (size < 1) return;
        int zeroth = array[0];
        for (int i = 0; i < size - 1; ++i) array[i] = array[i + 1];
        array[size - 1] = zeroth;
    }
