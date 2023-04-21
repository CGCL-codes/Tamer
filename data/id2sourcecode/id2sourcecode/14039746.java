    protected static Long[] removeFromIDArray(Long[] array, Long el) {
        int index = -1;
        for (int n = 0; n < array.length; n++) {
            if (array[n] == el) {
                index = n;
                break;
            }
        }
        assert index != -1;
        Long[] retArray = new Long[array.length - 1];
        for (int n = 0; n < retArray.length; n++) {
            if (n < index) {
                retArray[n] = array[n];
            } else {
                retArray[n] = array[n + 1];
            }
        }
        return retArray;
    }
