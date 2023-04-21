    public int getLineOfOffset(int offset) {
        int start = 0;
        int end = lineCount - 1;
        for (; ; ) {
            switch(end - start) {
                case 0:
                    if (getLineEndOffset(start) <= offset) return start + 1; else return start;
                case 1:
                    if (getLineEndOffset(start) <= offset) {
                        if (getLineEndOffset(end) <= offset) return end + 1; else return end;
                    } else return start;
                default:
                    int pivot = (end + start) / 2;
                    int value = getLineEndOffset(pivot);
                    if (value == offset) return pivot + 1; else if (value < offset) start = pivot + 1; else end = pivot - 1;
                    break;
            }
        }
    }
