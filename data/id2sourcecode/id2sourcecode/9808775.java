    public static boolean writePartOfFile(final String path, final long pos, byte[] input) throws IOException {
        try {
            RandomAccessFile raf;
            raf = writefilesCache.get(path);
            if (writePathes.contains(path)) {
                if (raf == null) writeMisses++; else writeHits++;
            }
            if (raf == null) {
                raf = new RandomAccessFile(path, "rw");
                writefilesCache.put(path, raf);
                writePathes.add(path);
            }
            FileChannel fc;
            if ((fc = writefileChannelCache.get(path)) == null) {
                fc = raf.getChannel();
                writefileChannelCache.put(path, fc);
            }
            long end = pos + input.length;
            fc.position(pos);
            FileLock fl = fc.tryLock(pos, end, false);
            if (!fl.isValid()) log.info("Lock invalid on " + path + ". Writing anyway");
            ByteBuffer bb = ByteBuffer.wrap(input);
            fc.write(bb);
            fl.release();
            return true;
        } catch (FileNotFoundException e) {
            log.warn("", e);
        }
        return false;
    }
