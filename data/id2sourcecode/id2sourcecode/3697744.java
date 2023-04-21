    public void stop() {
        selectionThread.stop();
        while (selectionThread.hasStopped() == false) {
        }
        for (Connection c : connections) {
            try {
                c.getChannel().close();
            } catch (Exception e) {
            }
        }
        connections.clear();
        try {
            serverSocketChannel.close();
        } catch (Exception e) {
        }
        try {
            selector.close();
        } catch (Exception e) {
        }
    }
