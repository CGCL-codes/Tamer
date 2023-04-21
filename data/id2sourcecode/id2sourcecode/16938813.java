        public void write(int b) throws IOException {
            if (bytes > 0 && writeBlock == null) writeBlock = (readBlock != null && writeBlockId.equals(readBlockId)) ? readBlock : (Block) container.get(writeBlockId, false);
            if (writeBlock == null || writeBlockOffset == blockSize) {
                Block newWriteBlock = new Block(new byte[blockSize], 0, blockSize);
                Object newWriteBlockId = container.insert(dummyBlock, false);
                if (writeBlock != null) {
                    byte[] byteArray = Converters.toByteArray(container.objectIdConverter(), newWriteBlockId);
                    System.arraycopy(writeBlock.array, writeBlock.offset + blockSize - byteArray.length, newWriteBlock.array, ShortConverter.SIZE, byteArray.length);
                    if (writeBlockId.equals(readBlockId) && readBlockOffset >= blockSize - byteArray.length) {
                        container.remove(readBlockId);
                        readBlockId = newWriteBlockId;
                        readBlockOffset -= blockSize - byteArray.length - ShortConverter.SIZE;
                        readBlock = newWriteBlock;
                    } else {
                        writeBlock.dataOutputStream().writeShort((short) byteArray.length);
                        System.arraycopy(byteArray, 0, writeBlock.array, writeBlock.offset + blockSize - byteArray.length, byteArray.length);
                        container.update(writeBlockId, writeBlock);
                    }
                    writeBlockOffset = ShortConverter.SIZE + byteArray.length;
                } else {
                    readBlockId = newWriteBlockId;
                    writeBlockOffset = ShortConverter.SIZE;
                }
                writeBlockId = newWriteBlockId;
                writeBlock = newWriteBlock;
            }
            writeBlock.set(writeBlockOffset++, (byte) b);
            bytes++;
        }
