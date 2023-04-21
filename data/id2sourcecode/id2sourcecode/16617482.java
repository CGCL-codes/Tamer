    public final void sort(int from, int to) {
        if (from >= to) {
            return;
        }
        int mid = (from + to) / 2;
        T tmp;
        T middle = objects[mid];
        if (objects[from].compareTo(middle) > 0) {
            objects[mid] = objects[from];
            objects[from] = middle;
            middle = objects[mid];
        }
        if (middle.compareTo(objects[to]) > 0) {
            objects[mid] = objects[to];
            objects[to] = middle;
            middle = objects[mid];
            if (objects[from].compareTo(middle) > 0) {
                objects[mid] = objects[from];
                objects[from] = middle;
                middle = objects[mid];
            }
        }
        int left = from + 1;
        int right = to - 1;
        if (left >= right) {
            return;
        }
        for (; ; ) {
            while (objects[right].compareTo(middle) > 0) {
                right--;
            }
            while (left < right && objects[left].compareTo(middle) <= 0) {
                left++;
            }
            if (left < right) {
                tmp = objects[left];
                objects[left] = objects[right];
                objects[right] = tmp;
                right--;
            } else {
                break;
            }
        }
        sort(from, left);
        sort(left + 1, to);
    }
