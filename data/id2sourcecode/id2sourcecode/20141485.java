    private boolean shouldProcessEvent(GraphEvent e) {
        if (!active) {
            return false;
        }
        return e.getSource() == context.getChannel() && e.getPostedBy() != context && e.getPostedBy() != context.getChannel();
    }
