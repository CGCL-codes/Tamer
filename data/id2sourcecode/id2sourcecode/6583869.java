    public void test2_1() {
        Integer[] arr1 = { 21, 32, 44, 55 };
        int[] arr2 = new int[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            arr2[i] = arr1[i + 1];
        }
    }
