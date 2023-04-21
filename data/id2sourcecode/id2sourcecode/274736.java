    private void start() {
        objectpath = new ObjectPath(file);
        if (localfile == null) {
            localfile = objectpath.getName();
        }
        long start, end;
        try {
            esu = new EsuRestApi(host, port, uid, secret);
            MetadataList smeta = esu.getSystemMetadata(objectpath, null);
            filesize = Long.parseLong(smeta.getMetadata("size").getValue());
            progress = 0;
            start = System.currentTimeMillis();
            File outFile = new File(localfile);
            RandomAccessFile raf = new RandomAccessFile(outFile, "rw");
            raf.setLength(filesize);
            FileChannel channel = raf.getChannel();
            long blockcount = filesize / blocksize;
            if (filesize % blocksize != 0) {
                blockcount++;
            }
            BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
            ThreadPoolExecutor pool = new ThreadPoolExecutor(threads, threads, 15, TimeUnit.SECONDS, queue);
            blocksRemaining = Collections.synchronizedSet(new HashSet<DownloadBlock>());
            for (long i = 0; i < blockcount; i++) {
                long offset = i * blocksize;
                long size = blocksize;
                if (offset + size > filesize) {
                    size = filesize - offset;
                }
                Extent extent = new Extent(offset, size);
                DownloadBlock b = new DownloadBlock();
                b.setChannel(channel);
                b.setEsu(esu);
                b.setExtent(extent);
                b.setListener(this);
                b.setPath(objectpath);
                blocksRemaining.add(b);
                pool.submit(b);
            }
            while (blocksRemaining.size() > 0) {
                Thread.sleep(500);
            }
            end = System.currentTimeMillis();
            long secs = ((end - start) / 1000);
            long rate = filesize / secs;
            System.out.println();
            System.out.println("Downloaded " + filesize + " bytes in " + secs + " seconds (" + rate + " bytes/s)");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
