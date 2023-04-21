                    public void close() {
                        if (readBlock != null) {
                            readBlock = null;
                            if (BlockBasedQueue.this.writeBlockId == null || !BlockBasedQueue.this.readBlockId.equals(BlockBasedQueue.this.writeBlockId)) BlockBasedQueue.this.container.unfix(BlockBasedQueue.this.readBlockId);
                        }
                    }
