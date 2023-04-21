    public TreeNode loadTreeFromFile(BufferedReader reader, String filename) {
        String prefix;
        try {
            prefix = reader.readLine();
        } catch (IOException e) {
            Constants.LOGGER.log(Level.WARNING, "Could not read/write PGrid Tree initialization file '" + filename + "'!", e);
            return null;
        }
        if (prefix.equals("")) return null;
        TreeNode leftChild = loadTreeFromFile(reader, filename);
        TreeNode rightChild = loadTreeFromFile(reader, filename);
        int leaves = (leftChild == null ? 0 : leftChild.getLeavesCount());
        leaves += (rightChild == null ? 0 : rightChild.getLeavesCount());
        int leftDepth = (leftChild == null ? 0 : leftChild.getDepth());
        int rightDepth = (rightChild == null ? 0 : rightChild.getDepth());
        int depth = Math.max(leftDepth, rightDepth);
        int nodes = 1;
        nodes += (leftChild == null ? 0 : leftChild.getNodesCount());
        nodes += (rightChild == null ? 0 : rightChild.getNodesCount());
        return new TreeNode(prefix, leftChild, rightChild, leaves, depth, nodes);
    }
