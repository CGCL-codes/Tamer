    public void put_element(int i, Object o) {
        int low = 0;
        int high = cache_size;
        int pos = 0;
        int j;
        high = lookup(i);
        if (high == cache_size) {
            pos = high - 1;
        } else {
            pos = high;
        }
        ;
        if ((index[pos] != i) || (last_used[pos] == 0)) {
            if (index[pos] == i) {
                low = pos;
            } else {
                low = get_lru_pos();
            }
            ;
            Object[] my_elements = elements;
            long[] my_last_used = last_used;
            int[] my_index = index;
            if (high <= low) {
                for (j = low; j > high; j--) {
                    my_elements[j] = my_elements[j - 1];
                    my_index[j] = my_index[j - 1];
                    my_last_used[j] = my_last_used[j - 1];
                }
                ;
            } else {
                for (j = low; j < high - 1; j++) {
                    my_elements[j] = my_elements[j + 1];
                    my_index[j] = my_index[j + 1];
                    my_last_used[j] = my_last_used[j + 1];
                }
                ;
                high--;
            }
            ;
            pos = high;
            my_elements[high] = o;
            my_index[high] = i;
        }
        ;
        counter++;
        last_used[pos] = counter;
    }
