    public FileChunkStore() {
        SDFSLogger.getLog().info("Opening Chunk Store");
        Arrays.fill(FREE, (byte) 0);
        try {
            if (!chunk_location.exists()) {
                chunk_location.mkdirs();
            }
            f = new File(chunk_location + File.separator + "chunks.chk");
            if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
            this.name = "chunks";
            p = f.toPath();
            chunkDataWriter = new RandomAccessFile(f, "rw");
            this.currentLength = chunkDataWriter.length();
            this.closed = false;
            fc = chunkDataWriter.getChannel();
            SDFSLogger.getLog().info("ChunkStore " + f.getPath() + " created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
