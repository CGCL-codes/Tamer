    public int getLineNumber(int position) {
        if (this.lineEndTable == null) return -2;
        int length;
        if ((length = this.lineEndTable.length) == 0) {
            if (position >= getStartPosition() + getLength()) {
                return -1;
            }
            return 1;
        }
        int low = 0;
        if (position < 0) {
            return -1;
        }
        if (position <= this.lineEndTable[low]) {
            return 1;
        }
        int hi = length - 1;
        if (position > this.lineEndTable[hi]) {
            if (position >= getStartPosition() + getLength()) {
                return -1;
            } else {
                return length + 1;
            }
        }
        while (true) {
            if (low + 1 == hi) {
                return low + 2;
            }
            int mid = low + (hi - low) / 2;
            if (position <= this.lineEndTable[mid]) {
                hi = mid;
            } else {
                low = mid;
            }
        }
    }
