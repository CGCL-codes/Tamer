    Code findCode(int value, Code[] codes) {
        int i, j, k;
        i = -1;
        j = codes.length;
        while (true) {
            k = (j + i) / 2;
            if (value < codes[k].min) {
                j = k;
            } else if (value > codes[k].max) {
                i = k;
            } else {
                return codes[k];
            }
        }
    }
