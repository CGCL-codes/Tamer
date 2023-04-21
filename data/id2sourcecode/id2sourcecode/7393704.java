    private void setSourceParams(AudioNode src) {
        soundPool.setLoop(src.getChannel(), src.isLooping() ? -1 : 0);
        soundPool.setVolume(src.getChannel(), src.getVolume(), src.getVolume());
    }
