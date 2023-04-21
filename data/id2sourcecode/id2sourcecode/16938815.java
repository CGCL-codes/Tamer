    public BlockBasedQueue(Container container, int blockSize, Converter converter, Function newObject, Function inputBufferSize, Function outputBufferSize, int size, int bytes, Object readBlockId, int readBlockOffset, Object writeBlockId, int writeBlockOffset) {
        super(converter, newObject, inputBufferSize, outputBufferSize, null, null);
        this.container = container;
        this.blockSize = blockSize;
        this.size = size;
        this.bytes = bytes;
        this.readBlockId = readBlockId;
        this.readBlockOffset = readBlockOffset;
        this.writeBlockId = writeBlockId;
        this.writeBlockOffset = writeBlockOffset;
        this.newInputStream = new AbstractFunction() {

            public Object invoke() {
                return new InputStream() {

                    public int read() throws IOException {
                        if (BlockBasedQueue.this.bytes == 0) return -1; else {
                            int b;
                            if (readBlock == null) readBlock = (writeBlock != null && BlockBasedQueue.this.readBlockId.equals(BlockBasedQueue.this.writeBlockId)) ? writeBlock : (Block) BlockBasedQueue.this.container.get(BlockBasedQueue.this.readBlockId, false);
                            b = readBlock.get(BlockBasedQueue.this.readBlockOffset++) & 255;
                            if (--BlockBasedQueue.this.bytes == 0) {
                                BlockBasedQueue.this.container.remove(BlockBasedQueue.this.readBlockId);
                                readBlock = null;
                                BlockBasedQueue.this.readBlockOffset = ShortConverter.SIZE;
                                writeBlock = null;
                                BlockBasedQueue.this.writeBlockOffset = ShortConverter.SIZE;
                            } else if (BlockBasedQueue.this.readBlockOffset == BlockBasedQueue.this.blockSize - readBlock.dataInputStream().readShort()) removeBlock();
                            return b;
                        }
                    }

                    public int available() {
                        return BlockBasedQueue.this.bytes;
                    }

                    public void close() {
                        if (readBlock != null) {
                            readBlock = null;
                            if (BlockBasedQueue.this.writeBlockId == null || !BlockBasedQueue.this.readBlockId.equals(BlockBasedQueue.this.writeBlockId)) BlockBasedQueue.this.container.unfix(BlockBasedQueue.this.readBlockId);
                        }
                    }
                };
            }
        };
        this.newOutputStream = new AbstractFunction() {

            public Object invoke() {
                return new OutputStream() {

                    public void write(int b) throws IOException {
                        if (BlockBasedQueue.this.bytes > 0 && writeBlock == null) writeBlock = (readBlock != null && BlockBasedQueue.this.writeBlockId.equals(BlockBasedQueue.this.readBlockId)) ? readBlock : (Block) BlockBasedQueue.this.container.get(BlockBasedQueue.this.writeBlockId, false);
                        if (writeBlock == null || BlockBasedQueue.this.writeBlockOffset == BlockBasedQueue.this.blockSize) {
                            Block newWriteBlock = new Block(new byte[BlockBasedQueue.this.blockSize], 0, BlockBasedQueue.this.blockSize);
                            Object newWriteBlockId = BlockBasedQueue.this.container.insert(dummyBlock, false);
                            if (writeBlock != null) {
                                byte[] byteArray = Converters.toByteArray(BlockBasedQueue.this.container.objectIdConverter(), newWriteBlockId);
                                System.arraycopy(writeBlock.array, writeBlock.offset + BlockBasedQueue.this.blockSize - byteArray.length, newWriteBlock.array, ShortConverter.SIZE, byteArray.length);
                                if (BlockBasedQueue.this.writeBlockId.equals(BlockBasedQueue.this.readBlockId) && BlockBasedQueue.this.readBlockOffset >= BlockBasedQueue.this.blockSize - byteArray.length) {
                                    BlockBasedQueue.this.container.remove(BlockBasedQueue.this.readBlockId);
                                    BlockBasedQueue.this.readBlockId = newWriteBlockId;
                                    BlockBasedQueue.this.readBlockOffset -= BlockBasedQueue.this.blockSize - byteArray.length - ShortConverter.SIZE;
                                    readBlock = newWriteBlock;
                                } else {
                                    writeBlock.dataOutputStream().writeShort((short) byteArray.length);
                                    System.arraycopy(byteArray, 0, writeBlock.array, writeBlock.offset + BlockBasedQueue.this.blockSize - byteArray.length, byteArray.length);
                                    BlockBasedQueue.this.container.update(BlockBasedQueue.this.writeBlockId, writeBlock);
                                }
                                BlockBasedQueue.this.writeBlockOffset = ShortConverter.SIZE + byteArray.length;
                            } else {
                                BlockBasedQueue.this.readBlockId = newWriteBlockId;
                                BlockBasedQueue.this.writeBlockOffset = ShortConverter.SIZE;
                            }
                            BlockBasedQueue.this.writeBlockId = newWriteBlockId;
                            writeBlock = newWriteBlock;
                        }
                        writeBlock.set(BlockBasedQueue.this.writeBlockOffset++, (byte) b);
                        BlockBasedQueue.this.bytes++;
                    }

                    public void close() {
                        if (writeBlock != null) {
                            BlockBasedQueue.this.container.update(BlockBasedQueue.this.writeBlockId, writeBlock);
                            writeBlock = null;
                            if (BlockBasedQueue.this.readBlockId == null || !BlockBasedQueue.this.readBlockId.equals(BlockBasedQueue.this.writeBlockId)) BlockBasedQueue.this.container.unfix(BlockBasedQueue.this.writeBlockId);
                        }
                    }
                };
            }
        };
    }
