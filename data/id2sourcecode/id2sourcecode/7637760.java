    public final void create(final int theInputCount, final int theIdealCount) {
        try {
            this.inputCount = theInputCount;
            this.idealCount = theIdealCount;
            final double[] input = new double[inputCount];
            final double[] ideal = new double[idealCount];
            this.file.delete();
            this.raf = new RandomAccessFile(this.file, "rw");
            this.fc = this.raf.getChannel();
            this.headerBuffer.clear();
            this.headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
            this.headerBuffer.put((byte) 'E');
            this.headerBuffer.put((byte) 'N');
            this.headerBuffer.put((byte) 'C');
            this.headerBuffer.put((byte) 'O');
            this.headerBuffer.put((byte) 'G');
            this.headerBuffer.put((byte) '-');
            this.headerBuffer.put((byte) '0');
            this.headerBuffer.put((byte) '0');
            this.headerBuffer.putDouble(input.length);
            this.headerBuffer.putDouble(ideal.length);
            this.numberOfRecords = 0;
            this.recordCount = this.inputCount + this.idealCount + 1;
            this.recordSize = this.recordCount * EncogEGBFile.DOUBLE_SIZE;
            this.recordBuffer = ByteBuffer.allocate(this.recordSize);
            this.headerBuffer.flip();
            this.fc.write(this.headerBuffer);
        } catch (final IOException ex) {
            throw new BufferedDataError(ex);
        }
    }
