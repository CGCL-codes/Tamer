    public final void open() {
        try {
            this.raf = new RandomAccessFile(this.file, "rw");
            this.fc = this.raf.getChannel();
            this.headerBuffer.clear();
            this.headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
            boolean isEncogFile = true;
            this.fc.read(this.headerBuffer);
            this.headerBuffer.position(0);
            isEncogFile = isEncogFile ? this.headerBuffer.get() == 'E' : false;
            isEncogFile = isEncogFile ? this.headerBuffer.get() == 'N' : false;
            isEncogFile = isEncogFile ? this.headerBuffer.get() == 'C' : false;
            isEncogFile = isEncogFile ? this.headerBuffer.get() == 'O' : false;
            isEncogFile = isEncogFile ? this.headerBuffer.get() == 'G' : false;
            isEncogFile = isEncogFile ? this.headerBuffer.get() == '-' : false;
            if (!isEncogFile) {
                throw new BufferedDataError("File is not a valid Encog binary file:" + this.file.toString());
            }
            final char v1 = (char) this.headerBuffer.get();
            final char v2 = (char) this.headerBuffer.get();
            final String versionStr = "" + v1 + v2;
            try {
                final int version = Integer.parseInt(versionStr);
                if (version > 0) {
                    throw new BufferedDataError("File is from a newer version of Encog than is currently in use.");
                }
            } catch (final NumberFormatException ex) {
                throw new BufferedDataError("File has invalid version number.");
            }
            this.inputCount = (int) this.headerBuffer.getDouble();
            this.idealCount = (int) this.headerBuffer.getDouble();
            this.recordCount = this.inputCount + this.idealCount + 1;
            this.recordSize = this.recordCount * EncogEGBFile.DOUBLE_SIZE;
            if (this.recordSize == 0) {
                this.numberOfRecords = 0;
            } else {
                this.numberOfRecords = (int) ((this.file.length() - EncogEGBFile.HEADER_SIZE) / this.recordSize);
            }
            this.recordBuffer = ByteBuffer.allocate(this.recordSize);
        } catch (final IOException ex) {
            throw new BufferedDataError(ex);
        }
    }
