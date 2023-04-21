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
