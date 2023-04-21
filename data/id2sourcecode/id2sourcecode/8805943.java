    private HotspotEvent removeEvent(HotspotEvent event) {
        boolean directRemove = expectedEvents.remove(event);
        if (directRemove || (!(event instanceof MessageEvent)) || (!smartCheck)) {
            return directRemove ? event : null;
        }
        MessageEvent actual = (MessageEvent) event;
        if (actual.getMessage() == null) {
            return null;
        }
        for (HotspotEvent e : expectedEvents) {
            if (e instanceof MessageEvent) {
                MessageEvent found = (MessageEvent) e;
                boolean equals = new EqualsBuilder().append(found.getType(), actual.getType()).append(found.getPlayer(), actual.getPlayer()).append(found.getChannel(), actual.getChannel()).isEquals() && messagesEquals(found.getMessage(), actual.getMessage());
                if (equals) {
                    expectedEvents.remove(found);
                    return found;
                }
            }
        }
        return null;
    }
