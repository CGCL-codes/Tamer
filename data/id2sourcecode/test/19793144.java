    protected void computeNodeValues() throws NoSuchAlgorithmException {
        int firstNode = firstLeaf() - 1;
        for (int i = firstNode; i >= ROOT_NODE; --i) {
            byte[] nodeDigest = CCNDigestHelper.digest(digestAlgorithm(), get(leftChild(i)), get(rightChild(i)));
            _tree[i - 1] = new DEROctetString(nodeDigest);
        }
    }
