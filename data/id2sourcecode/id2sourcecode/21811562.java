    private boolean verify(int pieceNum, boolean slow) throws IOException, InterruptedException {
        MessageDigest md = context.getMetaInfo().getMessageDigest();
        md.reset();
        int pieceSize = getPieceSize(pieceNum);
        byte[] buf = new byte[Math.min(65536, pieceSize)];
        int read = 0;
        long offset = (long) pieceNum * context.getMetaInfo().getPieceLength();
        while (read < pieceSize) {
            int readNow = diskController.read(offset, buf, 0, buf.length);
            if (readNow == 0) return false;
            long start = System.nanoTime();
            md.update(buf, 0, readNow);
            if (slow && SystemUtils.getIdleTime() < URN.MIN_IDLE_TIME && SharingSettings.FRIENDLY_HASHING.getValue()) {
                long interval = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                interval *= QUEUE.size() > 0 ? 5 : 3;
                if (interval > 0) Thread.sleep(interval); else Thread.yield();
            }
            read += readNow;
            offset += readNow;
        }
        byte[] sha1 = md.digest();
        return context.getMetaInfo().verify(sha1, pieceNum);
    }
