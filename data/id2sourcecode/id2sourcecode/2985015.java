    @Override
    List<NodeChannelRef> getItems() {
        if (_items == null) {
            _items = new ArrayList<NodeChannelRef>();
            for (AcceleratorNode node : _nodes) {
                for (String handle : _handles) {
                    try {
                        final Channel channel = node.getChannel(handle);
                        if (channel != null) {
                            _items.add(new NodeChannelRef(node, handle));
                        }
                    } catch (NoSuchChannelException exception) {
                    }
                }
            }
            sortItems(_items);
        }
        return _items;
    }
