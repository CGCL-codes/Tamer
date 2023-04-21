    @Override
    List<String> getItems() {
        if (_items == null) {
            _items = new ArrayList<String>();
            for (NodeChannelRef channelRef : _channelRefs) {
                _items.add(channelRef.getChannel().channelName());
            }
            sortItems(_items);
        }
        return _items;
    }
