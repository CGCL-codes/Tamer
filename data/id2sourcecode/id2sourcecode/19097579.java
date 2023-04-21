    public int searchUShort(int startIndex, int startOffset, int endIndex, int endOffset, int length, int key) {
        int location = 0;
        int bottom = 0;
        int top = length;
        while (top != bottom) {
            location = (top + bottom) / 2;
            int locationStart = this.readUShort(startIndex + location * startOffset);
            if (key < locationStart) {
                top = location;
            } else {
                int locationEnd = this.readUShort(endIndex + location * endOffset);
                if (key <= locationEnd) {
                    return location;
                }
                bottom = location + 1;
            }
        }
        return -1;
    }
