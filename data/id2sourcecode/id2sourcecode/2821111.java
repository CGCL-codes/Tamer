    public synchronized Channel getChannel() {
        while (!newChannel) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("ConfigCubby.getChannels: exception");
                e.printStackTrace();
            }
        }
        newChannel = false;
        notifyAll();
        return channel;
    }
