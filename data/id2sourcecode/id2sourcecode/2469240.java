    public int getChannelPressure() {
        synchronized (control_mutex) {
            return channelpressure;
        }
    }
