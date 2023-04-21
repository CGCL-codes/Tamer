    boolean shouldProcessEvent(GraphEvent e) {
        return active && e.getSource() == context.getChannel() && e.getPostedBy() != context && e.getPostedBy() != context.getChannel();
    }
