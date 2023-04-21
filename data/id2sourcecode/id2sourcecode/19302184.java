    public boolean remove(Object obj) {
        for (int i = 0; i < current; i++) {
            if (obj.equals(list[i])) {
                Object[] newList = new Object[size - 1];
                for (int j = 0; j < i; j++) {
                    newList[j] = list[j];
                }
                for (int k = i; k < current - 1; k++) {
                    newList[k] = list[k + 1];
                }
                list = newList;
                --current;
                --size;
                return true;
            }
        }
        return false;
    }
