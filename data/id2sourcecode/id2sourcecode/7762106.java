    @Override
    public void unpack(ByteBuffer b) throws IOException {
        b.position(0);
        b.order(ByteOrder.LITTLE_ENDIAN);
        this.width = b.getShort();
        this.height = b.getShort();
        this.leftoffset = b.getShort();
        this.topoffset = b.getShort();
        this.columnofs = new int[this.width];
        this.columns = new column_t[this.width];
        C2JUtils.initArrayOfObjects(this.columns, column_t.class);
        int[] actualsizes = new int[columns.length];
        for (int i = 0; i < actualsizes.length - 1; i++) {
            actualsizes[i] = columnofs[i + 1] - columnofs[i];
        }
        DoomBuffer.readIntArray(b, this.columnofs, this.columnofs.length);
        for (int i = 0; i < this.width; i++) {
            b.position(this.columnofs[i]);
            try {
                this.columns[i].unpack(b);
            } catch (Exception e) {
                if (i == 0) this.columns[i] = getBadColumn(this.height); else this.columns[i] = this.columns[i - 1];
            }
        }
    }
