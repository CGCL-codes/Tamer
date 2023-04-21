    public boolean isCorrupt(Range in, RandomAccessFile raf, byte[] tmp) {
        assert in.getHigh() <= FILE_SIZE : "invalid range " + in + " vs " + FILE_SIZE;
        if (in.getLow() % _nodeSize == 0 && in.getHigh() - in.getLow() + 1 <= _nodeSize && (in.getHigh() == in.getLow() + _nodeSize - 1 || in.getHigh() == FILE_SIZE - 1)) {
            try {
                MerkleTree digest = new MerkleTree(new Tiger());
                long read = in.getLow();
                while (read <= in.getHigh()) {
                    int size = (int) Math.min(tmp.length, in.getHigh() - read + 1);
                    synchronized (raf) {
                        raf.seek(read);
                        raf.readFully(tmp, 0, size);
                    }
                    digest.update(tmp, 0, size);
                    read += size;
                }
                byte[] hash = digest.digest();
                byte[] treeHash = NODES.get((int) (in.getLow() / _nodeSize));
                boolean ok = Arrays.equals(treeHash, hash);
                if (LOG.isDebugEnabled()) LOG.debug("interval " + in + " verified " + ok);
                return !ok;
            } catch (IOException assumeCorrupt) {
                LOG.debug("iox while verifying ", assumeCorrupt);
                return true;
            }
        }
        return true;
    }
