    public void append(String s) {
        if (s.length() == 0) return;
        for (int i = 0; i < count; i++) {
            if (s.equals(strings[i])) {
                for (int j = i + 1; j < count; j++) strings[j - 1] = strings[j];
                --count;
                break;
            }
        }
        if (count == limit) {
            for (int i = 0; i < limit - 1; i++) strings[i] = strings[i + 1];
            --count;
        }
        strings[count++] = s;
        reset();
    }
