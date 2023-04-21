    @Override
    protected TaskResult execute() throws Exception {
        try {
            Date remoteMtime = null;
            if (ent.getUserMetadata().getMetadata(AtmosSync.MTIME_NAME) != null) {
                remoteMtime = new Date(Long.parseLong(ent.getUserMetadata().getMetadata(AtmosSync.MTIME_NAME).getValue()));
            } else {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                remoteMtime = df.parse(ent.getSystemMetadata().getMetadata("mtime").getValue());
            }
            long filesize = Long.parseLong(ent.getSystemMetadata().getMetadata("size").getValue());
            if (file.exists() && !atmosSync.isForce()) {
                if (file.lastModified() == remoteMtime.getTime() && file.length() == filesize) {
                    l4j.info("Files are equal " + this);
                    atmosSync.success(this, file, ent.getPath(), 0);
                    return new TaskResult(true);
                }
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel channel = raf.getChannel();
            long blockcount = filesize / CHUNK_SIZE;
            if (filesize % CHUNK_SIZE != 0) {
                blockcount++;
            }
            Set<DownloadBlockTask> blocks = new HashSet<DownloadBlockTask>();
            DownloadCompleteTask dct = new DownloadCompleteTask();
            for (long i = 0; i < blockcount; i++) {
                long offset = i * CHUNK_SIZE;
                long size = CHUNK_SIZE;
                if (offset + size > filesize) {
                    size = filesize - offset;
                }
                Extent extent = new Extent(offset, size);
                DownloadBlockTask b = new DownloadBlockTask();
                b.setChannel(channel);
                b.setEsu(atmosSync.getEsu());
                b.setExtent(extent);
                b.setListener(dct);
                b.setPath(ent.getPath());
                dct.addParent(b);
                blocks.add(b);
                b.addParent(this);
                b.addToGraph(atmosSync.getGraph());
            }
            dct.setChannel(channel);
            dct.setBlocks(blocks);
            dct.setAtmosSync(atmosSync);
            dct.setPath(ent.getPath());
            dct.setFile(file);
            dct.addToGraph(atmosSync.getGraph());
            dct.setSize(filesize);
            dct.setMtime(remoteMtime);
            return new TaskResult(true);
        } catch (Exception e) {
            atmosSync.failure(this, file, ent.getPath(), e);
            return new TaskResult(false);
        }
    }
