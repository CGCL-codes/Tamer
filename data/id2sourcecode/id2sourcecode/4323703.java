    private void startImmediately(StartSchedulerEntry entry) {
        if (Debug.getTraceStartScheduler()) {
            Debug.out("StartScheduler.startImmediately(" + entry + "): called");
        }
        AudioInputStream audioInputStream = entry.getAudioInputStream();
        int nChannel = entry.getChannel();
        AudioChannel channel = getAudioChannel(nChannel);
        channel.addAudioInputStream(audioInputStream);
    }
