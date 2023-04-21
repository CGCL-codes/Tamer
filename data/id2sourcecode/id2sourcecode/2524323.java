        return newArray;
    }

    public static String[] deleteString(String array[], int n) {
        int i;
        String newArray[] = new String[array.length - 1];
        for (i = 0; i < n; i++) newArray[i] = array[i];
