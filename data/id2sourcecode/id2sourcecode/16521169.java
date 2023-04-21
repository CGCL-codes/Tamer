    final void checkItems(ChannelIF newChannel, ChannelRecord record) {
        ChannelIF existingChannel = record.getChannel();
        Collection<ItemIF> items = newChannel.getItems();
        final ItemIF[] newItems = (ItemIF[]) items.toArray(new ItemIF[items.size()]);
        final Collection currentItems = existingChannel.getItems();
        boolean finish = false;
        for (int i = 0; !record.isCanceled() && !finish && i < newItems.length; i++) {
            final ItemIF newItem = newItems[i];
            if (!record.isCanceled() && !currentItems.contains(newItem)) {
                doAdditionIfApproved(newItem, existingChannel);
            } else if (itemScanningPolicy == POLICY_SKIP_AFTER_EXISTING) {
                finish = true;
            }
        }
    }
