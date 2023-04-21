        public void close() {
            if (writeBlock != null) {
                container.update(writeBlockId, writeBlock);
                writeBlock = null;
                if (readBlockId == null || !readBlockId.equals(writeBlockId)) container.unfix(writeBlockId);
            }
        }
