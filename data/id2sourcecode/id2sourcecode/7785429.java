    void swapInt() {
        byte i = event[index];
        sevent[index] = event[index + 3];
        sevent[index + 3] = i;
        i = event[index + 1];
        sevent[index + 1] = event[index + 2];
        sevent[index + 2] = i;
        index += 4;
    }
