    public int searchUShort(int startIndex, int startOffset, int length, int key) {
        int location = 0;
        int bottom = 0;
        int top = length;
        while (top != bottom) {
            location = (top + bottom) / 2;
            int locationStart = this.readUShort(startIndex + location * startOffset);
            if (key < locationStart) {
                top = location;
            } else if (key > locationStart) {
                bottom = location + 1;
            } else {
                return location;
            }
        }
        return -1;
    }
