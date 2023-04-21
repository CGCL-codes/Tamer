    private void sort(int left, int right) throws MXQueryException {
        if (left < right) {
            int leftRight = left + (right - left) / 2;
            int rightLeft = leftRight + 1;
            this.sort(left, leftRight);
            this.sort(rightLeft, right);
            int copyPos = left;
            int curLeft = left;
            int curRight = rightLeft;
            while (curLeft <= leftRight && curRight <= right) {
                if (this.comp.compare(this.arr[curLeft], this.arr[curRight]) <= 0) {
                    this.copyArr[copyPos] = this.arr[curLeft];
                    copyPos++;
                    curLeft++;
                } else {
                    this.copyArr[copyPos] = this.arr[curRight];
                    copyPos++;
                    curRight++;
                }
            }
            while (curLeft <= leftRight) {
                this.copyArr[copyPos] = this.arr[curLeft];
                copyPos++;
                curLeft++;
            }
            while (curRight <= right) {
                this.copyArr[copyPos] = this.arr[curRight];
                copyPos++;
                curRight++;
            }
            System.arraycopy(this.copyArr, left, this.arr, left, right - left + 1);
        }
    }
