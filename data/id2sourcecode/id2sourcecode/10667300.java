    public boolean shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return false;
        }
        boolean changed = false;
        int middle = (low + high) / 2;
        changed |= shuttlesort(to, from, low, middle);
        changed |= shuttlesort(to, from, middle, high);
        int p = low;
        int q = middle;
        if (high - low >= 4) {
            if (compare(from[middle - 1], from[middle]) <= 0) {
                for (int i = low; i < high; i++) {
                    to[i] = from[i];
                }
                return changed;
            } else {
                if (compare(from[high - 1], from[low]) < 0) {
                    int i = low;
                    for (; q < high; q++) {
                        to[i++] = from[q];
                    }
                    for (; i < high; i++) {
                        to[i] = from[p++];
                    }
                    return changed;
                }
            }
        }
        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                changed |= (p < middle);
                to[i] = from[q++];
            }
        }
        return changed;
    }
