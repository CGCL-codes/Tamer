    @Override
    public void setup(SymmetricDatabase db) {
        List<NodeChannel> nc = db.getChannels();
        list = new ArrayList<Channel>(nc.size());
        for (NodeChannel nodeChannel : nc) {
            list.add(nodeChannel);
        }
    }
