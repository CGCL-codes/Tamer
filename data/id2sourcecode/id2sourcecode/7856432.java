    public SCAudioClipImpl(URL url, AudioNotifierService audioNotifier) {
        InputStream inputstream;
        try {
            inputstream = url.openStream();
            this.createAppletAudioClip(inputstream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.audioListener = new PlayAudioListener(audioClip);
        this.playAudioTimer.addActionListener(audioListener);
        this.audioNotifier = audioNotifier;
    }
