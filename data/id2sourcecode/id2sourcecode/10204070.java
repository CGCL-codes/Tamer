    public void setAudioDeviceHandle(AudioDeviceHandle audio) {
        audioDeviceHandle = audio;
        nChannel = audioDeviceHandle.getChannels();
        System.out.println(audioDeviceHandle.toString() + "   " + nChannel);
    }
