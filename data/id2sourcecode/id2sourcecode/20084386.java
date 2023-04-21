    public float[] swapMembers(float[] array) {
        float tmp;
        for (int i = 0; i < array.length; i += 2) {
            tmp = array[i];
            array[i] = array[i + 1];
            array[i + 1] = tmp;
        }
        return array;
    }
