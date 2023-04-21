    @Override
    protected void decodeModeParameters(int dataLength, DataInputStream inputStream) throws BufferUnderflowException, IllegalArgumentException {
        try {
            int b = inputStream.readUnsignedByte();
            this.AWRE = ((b >>> 7) & 1) == 1;
            this.ARRE = ((b >>> 6) & 1) == 1;
            this.TB = ((b >>> 5) & 1) == 1;
            this.RC = ((b >>> 4) & 1) == 1;
            this.EER = ((b >>> 3) & 1) == 1;
            this.PER = ((b >>> 2) & 1) == 1;
            this.DTE = ((b >>> 1) & 1) == 1;
            this.DCR = (b & 1) == 1;
            this.readRetryCount = inputStream.readUnsignedByte();
            inputStream.readByte();
            inputStream.readByte();
            inputStream.readByte();
            inputStream.readByte();
            this.writeRetryCount = inputStream.readUnsignedByte();
            inputStream.readByte();
            this.recoveryTimeLimit = inputStream.readUnsignedShort();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading input data.");
        }
    }
