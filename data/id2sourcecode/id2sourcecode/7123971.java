        private void createSegment(Memory memory, RandomAccessFile file, long offset, int address, int filesize, int memsize, boolean read, boolean write, boolean exec) throws MemoryMapException {
            if (memsize < filesize) {
                throw new Error("Segment memory size (" + memsize + ")less than file size (" + filesize + ")");
            }
            if (filesize == 0) {
                memory.map(address, memsize, read, write, exec);
            } else {
                int alignedAddress;
                long alignedOffset;
                int alignedFilesize;
                if (memory.isPageAligned(address)) {
                    alignedAddress = address;
                    alignedOffset = offset;
                    alignedFilesize = filesize;
                } else {
                    alignedAddress = memory.truncateToPage(address);
                    int delta = address - alignedAddress;
                    alignedOffset = offset - delta;
                    alignedFilesize = filesize + delta;
                }
                memory.map(file, alignedOffset, alignedAddress, alignedFilesize, read, write, exec);
                if (filesize < memsize) {
                    alignedAddress = memory.truncateToNextPage(address + filesize);
                    int delta = alignedAddress - (address + filesize);
                    memory.map(alignedAddress, memsize - filesize - delta, read, write, exec);
                }
            }
        }
