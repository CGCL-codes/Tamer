                    public void close() {
                        if (writeBlock != null) {
                            BlockBasedQueue.this.container.update(BlockBasedQueue.this.writeBlockId, writeBlock);
                            writeBlock = null;
                            if (BlockBasedQueue.this.readBlockId == null || !BlockBasedQueue.this.readBlockId.equals(BlockBasedQueue.this.writeBlockId)) BlockBasedQueue.this.container.unfix(BlockBasedQueue.this.writeBlockId);
                        }
                    }
