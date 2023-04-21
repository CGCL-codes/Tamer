    private void textCompaction(byte[] input, int start, int length) {
        int dest[] = new int[ABSOLUTE_MAX_TEXT_SIZE * 2];
        int mode = ALPHA;
        int ptr = 0;
        int fullBytes = 0;
        int v = 0;
        int k;
        int size;
        length += start;
        for (k = start; k < length; ++k) {
            v = getTextTypeAndValue(input, length, k);
            if ((v & mode) != 0) {
                dest[ptr++] = v & 0xff;
                continue;
            }
            if ((v & ISBYTE) != 0) {
                if ((ptr & 1) != 0) {
                    dest[ptr++] = PAL;
                    mode = (mode & PUNCTUATION) != 0 ? ALPHA : mode;
                }
                dest[ptr++] = BYTESHIFT;
                dest[ptr++] = v & 0xff;
                fullBytes += 2;
                continue;
            }
            switch(mode) {
                case ALPHA:
                    if ((v & LOWER) != 0) {
                        dest[ptr++] = LL;
                        dest[ptr++] = v & 0xff;
                        mode = LOWER;
                    } else if ((v & MIXED) != 0) {
                        dest[ptr++] = ML;
                        dest[ptr++] = v & 0xff;
                        mode = MIXED;
                    } else if ((getTextTypeAndValue(input, length, k + 1) & getTextTypeAndValue(input, length, k + 2) & PUNCTUATION) != 0) {
                        dest[ptr++] = ML;
                        dest[ptr++] = PL;
                        dest[ptr++] = v & 0xff;
                        mode = PUNCTUATION;
                    } else {
                        dest[ptr++] = PS;
                        dest[ptr++] = v & 0xff;
                    }
                    break;
                case LOWER:
                    if ((v & ALPHA) != 0) {
                        if ((getTextTypeAndValue(input, length, k + 1) & getTextTypeAndValue(input, length, k + 2) & ALPHA) != 0) {
                            dest[ptr++] = ML;
                            dest[ptr++] = AL;
                            mode = ALPHA;
                        } else {
                            dest[ptr++] = AS;
                        }
                        dest[ptr++] = v & 0xff;
                    } else if ((v & MIXED) != 0) {
                        dest[ptr++] = ML;
                        dest[ptr++] = v & 0xff;
                        mode = MIXED;
                    } else if ((getTextTypeAndValue(input, length, k + 1) & getTextTypeAndValue(input, length, k + 2) & PUNCTUATION) != 0) {
                        dest[ptr++] = ML;
                        dest[ptr++] = PL;
                        dest[ptr++] = v & 0xff;
                        mode = PUNCTUATION;
                    } else {
                        dest[ptr++] = PS;
                        dest[ptr++] = v & 0xff;
                    }
                    break;
                case MIXED:
                    if ((v & LOWER) != 0) {
                        dest[ptr++] = LL;
                        dest[ptr++] = v & 0xff;
                        mode = LOWER;
                    } else if ((v & ALPHA) != 0) {
                        dest[ptr++] = AL;
                        dest[ptr++] = v & 0xff;
                        mode = ALPHA;
                    } else if ((getTextTypeAndValue(input, length, k + 1) & getTextTypeAndValue(input, length, k + 2) & PUNCTUATION) != 0) {
                        dest[ptr++] = PL;
                        dest[ptr++] = v & 0xff;
                        mode = PUNCTUATION;
                    } else {
                        dest[ptr++] = PS;
                        dest[ptr++] = v & 0xff;
                    }
                    break;
                case PUNCTUATION:
                    dest[ptr++] = PAL;
                    mode = ALPHA;
                    --k;
                    break;
            }
        }
        if ((ptr & 1) != 0) dest[ptr++] = PS;
        size = (ptr + fullBytes) / 2;
        if (size + cwPtr > MAX_DATA_CODEWORDS) {
            throw new IndexOutOfBoundsException("The text is too big.");
        }
        length = ptr;
        ptr = 0;
        while (ptr < length) {
            v = dest[ptr++];
            if (v >= 30) {
                codewords[cwPtr++] = v;
                codewords[cwPtr++] = dest[ptr++];
            } else codewords[cwPtr++] = v * 30 + dest[ptr++];
        }
    }
