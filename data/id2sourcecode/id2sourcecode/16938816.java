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
