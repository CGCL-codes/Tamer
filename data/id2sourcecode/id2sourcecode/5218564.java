    public void RemoveKey(int key) {
        if (buffer_size <= 0) return;
        boolean key_found = false;
        int i = 0;
        for (; i < buffer_size; i++) {
            if (buffer[i] == key) {
                key_found = true;
                break;
            }
        }
        if (!key_found) return;
        for (int j = i; j < (buffer_size - 1); j++) {
            buffer[j] = buffer[j + 1];
        }
        buffer_size--;
    }
