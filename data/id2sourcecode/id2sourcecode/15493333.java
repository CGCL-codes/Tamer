    public void sendSystemReset() {
        if (getChannels() != null) {
            for (int i = 0; i < getChannels().length; i++) {
                getChannels()[i].resetAllControllers();
            }
        }
    }
