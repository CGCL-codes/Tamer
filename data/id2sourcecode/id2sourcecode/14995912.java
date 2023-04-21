    public static String[] removeFirst(String[] arr) {
        String result[] = null;
        int length = arr.length - 1;
        if (length > 0) {
            result = new String[length];
            for (int i = 0; i < length; i++) {
                result[i] = arr[i + 1];
            }
        }
        return result;
    }
