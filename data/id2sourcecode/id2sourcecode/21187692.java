    public void load(int header, GPXByteBuffer srcBuffer) throws Throwable {
        if (header == HEADER_BCFS) {
            byte[] bcfsBytes = srcBuffer.readBytes(srcBuffer.length());
            int sectorSize = 0x1000;
            int offset = 0;
            while ((offset = (offset + sectorSize)) + 3 < bcfsBytes.length) {
                if (getInteger(bcfsBytes, offset) == 2) {
                    int indexFileName = (offset + 4);
                    int indexFileSize = (offset + 0x8C);
                    int indexOfBlock = (offset + 0x94);
                    int block = 0;
                    int blockCount = 0;
                    ByteArrayOutputStream fileBytesStream = new ByteArrayOutputStream();
                    while ((block = (getInteger(bcfsBytes, (indexOfBlock + (4 * (blockCount++)))))) != 0) {
                        fileBytesStream.write(getBytes(bcfsBytes, (offset = (block * sectorSize)), sectorSize));
                    }
                    int fileSize = getInteger(bcfsBytes, indexFileSize);
                    byte[] fileBytes = fileBytesStream.toByteArray();
                    if (fileBytes.length >= fileSize) {
                        this.fileSystem.add(new GPXFile(getString(bcfsBytes, indexFileName, 127), getBytes(fileBytes, 0, fileSize)));
                    }
                }
            }
        } else if (header == HEADER_BCFZ) {
            ByteArrayOutputStream bcfsBuffer = new ByteArrayOutputStream();
            int expectLength = getInteger(srcBuffer.readBytes(4), 0);
            while (!srcBuffer.end() && srcBuffer.offset() < expectLength) {
                int flag = srcBuffer.readBits(1);
                if (flag == 1) {
                    int bits = srcBuffer.readBits(4);
                    int offs = srcBuffer.readBitsReversed(bits);
                    int size = srcBuffer.readBitsReversed(bits);
                    byte[] bcfsBytes = bcfsBuffer.toByteArray();
                    int pos = (bcfsBytes.length - offs);
                    for (int i = 0; i < (size > offs ? offs : size); i++) {
                        bcfsBuffer.write(bcfsBytes[pos + i]);
                    }
                } else {
                    int size = srcBuffer.readBitsReversed(2);
                    for (int i = 0; i < size; i++) {
                        bcfsBuffer.write(srcBuffer.readBits(8));
                    }
                }
            }
            this.load(new ByteArrayInputStream(bcfsBuffer.toByteArray()));
        } else {
            throw new Exception("This is not a GPX file");
        }
    }
