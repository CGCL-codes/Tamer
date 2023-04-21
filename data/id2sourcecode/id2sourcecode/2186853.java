        public void write(int address, int length) {
            if (length <= 0 || !Memory.isAddressGood(address)) {
                return;
            }
            try {
                if (output == null) {
                    output = new RandomAccessFile(getFileName(), "rw");
                }
                if (byteBuffer == null || byteBuffer.capacity() < length) {
                    byteBuffer = ByteBuffer.allocateDirect(length);
                }
                byteBuffer.clear();
                byteBuffer.limit(length);
                Buffer memoryBuffer = Memory.getInstance().getBuffer(address, length);
                Utilities.putBuffer(byteBuffer, memoryBuffer, ByteOrder.LITTLE_ENDIAN);
                byteBuffer.rewind();
                output.getChannel().write(byteBuffer);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                Modules.log.error(e);
            }
        }
