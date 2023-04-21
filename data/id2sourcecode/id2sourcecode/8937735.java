    public List<OutgoingBatch> getBatchesForChannelWindows(Node targetNode, NodeChannel channel, List<NodeGroupChannelWindow> windows) {
        List<OutgoingBatch> keeping = new ArrayList<OutgoingBatch>();
        if (windows != null) {
            if (batches != null && batches.size() > 0) {
                if (inTimeWindow(windows, targetNode.getTimezoneOffset())) {
                    for (OutgoingBatch outgoingBatch : batches) {
                        if (channel.getChannelId().equals(outgoingBatch.getChannelId())) {
                            keeping.add(outgoingBatch);
                        }
                    }
                }
            }
        }
        return keeping;
    }
