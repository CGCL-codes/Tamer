    public boolean isCorrupt(Range in, byte[] data, int length) {
        assert (in.getHigh() <= FILE_SIZE);
        if (in.getLow() % _nodeSize == 0 && in.getHigh() - in.getLow() + 1 <= _nodeSize && (in.getHigh() == in.getLow() + _nodeSize - 1 || in.getHigh() == FILE_SIZE - 1)) {
            MerkleTree digest = new MerkleTree(new Tiger());
            digest.update(data, 0, length);
            byte[] hash = digest.digest();
            byte[] treeHash = NODES.get((int) (in.getLow() / _nodeSize));
            boolean ok = Arrays.equals(treeHash, hash);
            if (LOG.isDebugEnabled()) LOG.debug("interval " + in + " verified " + ok);
            return !ok;
        }
        return true;
    }
