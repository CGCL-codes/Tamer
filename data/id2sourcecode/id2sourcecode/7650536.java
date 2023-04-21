    public long setUp() throws IOException, HashtableFullException {
        File _fs = new File(fileName);
        boolean exists = new File(fileName).exists();
        if (!_fs.getParentFile().exists()) {
            _fs.getParentFile().mkdirs();
        }
        long endPos = 0;
        kRaf = new RandomAccessFile(fileName, this.fileParams);
        kFc = (FileChannelImpl) kRaf.getChannel();
        this.freeSlots.clear();
        long start = System.currentTimeMillis();
        int freeSl = 0;
        if (exists) {
            this.closed = false;
            SDFSLogger.getLog().info("This looks an existing hashtable will repopulate with [" + size + "] entries.");
            SDFSLogger.getLog().info("##################### Loading Hash Database #####################");
            kRaf.seek(0);
            int count = 0;
            System.out.print("Loading ");
            while (kFc.position() < kRaf.length()) {
                count++;
                if (count > 500000) {
                    count = 0;
                    System.out.print("#");
                }
                byte[] raw = new byte[ChunkData.RAWDL];
                try {
                    long currentPos = kFc.position();
                    kRaf.read(raw);
                    if (Arrays.equals(raw, BLANKCM)) {
                        SDFSLogger.getLog().debug("found free slot at " + ((currentPos / raw.length) * Main.chunkStorePageSize));
                        this.addFreeSlot((currentPos / raw.length) * Main.chunkStorePageSize);
                        freeSl++;
                    } else {
                        ChunkData cm = null;
                        boolean corrupt = false;
                        try {
                            cm = new ChunkData(raw);
                        } catch (Exception e) {
                            SDFSLogger.getLog().info("HashTable corrupt!");
                            corrupt = true;
                        }
                        long pos = (currentPos / raw.length) * Main.CHUNK_LENGTH;
                        if (cm.getcPos() != pos) SDFSLogger.getLog().warn("Possible Corruption at " + cm.getcPos() + " file position is " + pos);
                        if (!corrupt) {
                            long value = cm.getcPos();
                            if (cm.ismDelete()) {
                                this.addFreeSlot(cm.getcPos());
                                freeSl++;
                                this.kSz--;
                            } else {
                                boolean added = this.put(cm, false);
                                if (added) this.kSz++;
                                if (value > endPos) endPos = value + Main.CHUNK_LENGTH;
                            }
                        }
                    }
                } catch (BufferUnderflowException e) {
                }
            }
        }
        System.out.println();
        HashChunkService.getChuckStore().setSize(endPos);
        SDFSLogger.getLog().info("########## Finished Loading Hash Database in [" + (System.currentTimeMillis() - start) / 100 + "] seconds ###########");
        SDFSLogger.getLog().info("loaded [" + kSz + "] into the hashtable [" + this.fileName + "] free slots available are [" + freeSl + "] free slots added [" + this.freeSlots.cardinality() + "] end file position is [" + endPos + "]!");
        return size;
    }
