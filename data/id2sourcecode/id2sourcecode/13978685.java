    public String[] gerSourcesList() throws SAPIException {
        @SuppressWarnings("rawtypes") Iterator treeIterator = getChannelTree().iterator();
        LinkedList<String> sources = new LinkedList<String>();
        while (treeIterator.hasNext()) {
            ChannelTree.Node node = (ChannelTree.Node) treeIterator.next();
            if (node.getType() == ChannelTree.SOURCE) sources.add(node.getName());
        }
        return sources.toArray(new String[0]);
    }
