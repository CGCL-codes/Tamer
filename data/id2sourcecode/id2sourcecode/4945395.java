    public Input getInput(JDialog dialog) throws UserCancelled {
        SoundInputDialog sic = new SoundInputDialog(dialog);
        sic.setVisible(true);
        if (sic.isOK()) return new SimpleAudioInput(sic.getSampleRate(), sic.getSampleSize(), sic.getChannels(), sic.isSigned(), sic.isBigEndian()); else throw new UserCancelled();
    }
