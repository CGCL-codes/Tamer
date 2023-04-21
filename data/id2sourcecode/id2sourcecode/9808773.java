    public static byte[] readPartOfFile(final String path, final long pos, long end) {
        try {
            RandomAccessFile raf;
            raf = readfilesCache.get(path);
            if (readPathes.contains(path)) {
                if (raf == null) readMisses++; else readHits++;
            }
            if (raf == null) {
                raf = new RandomAccessFile(path, "r");
                readfilesCache.put(path, raf);
                readPathes.add(path);
            }
            if (raf.length() < end) end = raf.length();
            if (raf.length() < pos) throw new NullPointerException("Start Position is not in File!");
            FileChannel fc;
            if ((fc = readfileChannelCache.get(path)) == null) {
                fc = raf.getChannel();
                readfileChannelCache.put(path, fc);
            }
            fc.position(pos);
            ByteBuffer bb = ByteBuffer.allocate((int) ((end - pos)));
            fc.read(bb);
            return bb.array();
        } catch (FileNotFoundException e) {
            log.warn("", e);
        } catch (IOException e) {
            log.warn("", e);
        }
        return null;
    }
