    public synchronized Channel[] getChannels() {
        while (!newChannels) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("ConfigCubby.getChannels: exception");
                e.printStackTrace();
            }
        }
        newChannels = false;
        notifyAll();
        return channels;
    }
