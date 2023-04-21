    public Output getOutput(JDialog parent, int frequency) throws UserCancelled {
        SoundInputDialog sic = new SoundInputDialog(parent);
        sic.setVisible(true);
        if (sic.isOK()) return new SimpleAudioOutput(sic.getSampleRate(), sic.getSampleSize(), sic.getChannels(), sic.isSigned(), sic.isBigEndian()); else throw new UserCancelled();
    }
