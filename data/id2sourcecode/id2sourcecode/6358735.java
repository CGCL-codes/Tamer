    public long insertEntry(ReadableByteChannel entry) throws IOException {
        checkInsert();
        long pos = index.getLastOffset();
        FileChannel entryFile = new WorkAroundFileChannel(this.entryFile);
        while (true) {
            long amtTransfered = entryFile.transferFrom(entry, pos, 0x1000000);
            if (amtTransfered == 0) break;
            pos += amtTransfered;
        }
        return index.pushNext(pos - index.getLastOffset());
    }
