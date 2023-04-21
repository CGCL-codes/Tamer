    int search(int line) {
        if (line < fvm[0]) return -1;
        if (line >= fvm[fvmcount - 1]) return fvmcount - 1;
        if (lastfvmget != -1) {
            if (line >= fvm[lastfvmget]) {
                if (lastfvmget == fvmcount - 1 || line < fvm[lastfvmget + 1]) {
                    return lastfvmget;
                }
            }
        }
        int start = 0;
        int end = fvmcount - 1;
        loop: for (; ; ) {
            switch(end - start) {
                case 0:
                    lastfvmget = start;
                    break loop;
                case 1:
                    int value = fvm[end];
                    if (value <= line) lastfvmget = end; else lastfvmget = start;
                    break loop;
                default:
                    int pivot = (end + start) / 2;
                    value = fvm[pivot];
                    if (value == line) {
                        lastfvmget = pivot;
                        break loop;
                    } else if (value < line) start = pivot; else end = pivot - 1;
                    break;
            }
        }
        return lastfvmget;
    }
