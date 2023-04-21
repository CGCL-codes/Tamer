    public static byte[][] digest(FileInputStream fis) {
        try {
            int i;
            int c;
            ByteBuffer hashset;
            FileChannel fc = fis.getChannel();
            MD4 msgDigest = new MD4();
            ByteBuffer bb = ByteBuffer.allocateDirect(16384).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer di = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
            log.finest("pos:" + fc.position());
            c = (int) ((fc.size() + PARTSIZE - 1) / PARTSIZE);
            log.finer("hash " + c + " EdonkeyParts");
            hashset = ByteBuffer.allocate(16 * (c > 0 ? c : 1)).order(ByteOrder.LITTLE_ENDIAN);
            for (i = 1; i < c; i++) {
                log.finest("filechannel pos:" + fc.position() + " part: " + i + " of " + c);
                while (fc.position() <= (i * PARTSIZE - bb.capacity())) {
                    fc.read(bb);
                    bb.flip();
                    msgDigest.update(bb);
                    bb.rewind();
                }
                log.finest("filechannel pos:" + fc.position() + " part: " + i + " of " + c);
                if (fc.position() < (i * PARTSIZE)) {
                    bb.limit((int) ((i * PARTSIZE) - fc.position()));
                    fc.read(bb);
                    bb.flip();
                    msgDigest.update(bb);
                    bb.rewind();
                }
                hashset.limit(16 * i);
                msgDigest.finalDigest(hashset);
            }
            if (c > 0) {
                while (fc.position() < (fc.size())) {
                    fc.read(bb);
                    bb.flip();
                    msgDigest.update(bb);
                    bb.rewind();
                }
                hashset.limit(16 * i);
            }
            msgDigest.finalDigest(hashset);
            hashset.flip();
            if (c > 1) {
                msgDigest.update(hashset);
                msgDigest.finalDigest(di);
            } else {
                di.put(hashset);
            }
            di.rewind();
            hashset.rewind();
            byte[][] hashes = new byte[(c != 1) ? (c + 1) : 1][16];
            di.get(hashes[0]);
            for (int j = 1; j < hashes.length; j++) {
                hashset.get(hashes[j]);
            }
            hashset.rewind();
            log.finer("Hash: " + Convert.bytesToHexString(hashes[0]));
            for (int j = 1; j < hashes.length; j++) {
                log.finer("partial hash of part " + j + " is " + Convert.bytesToHexString(hashes[j]));
            }
            log.fine("ed2k: file done");
            return hashes;
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
        return null;
    }
