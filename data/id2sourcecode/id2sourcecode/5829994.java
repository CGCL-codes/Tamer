    public Input getInput(JDialog dialog) throws UserCancelled {
        VocInputDialog sic = new VocInputDialog(dialog);
        sic.setResizable(false);
        sic.setVisible(true);
        if (sic.isOK()) return new VocInput(sic.getFile(), sic.getSel_Channel(), sic.getChannels(), sic.getSampleRate(), sic.getSampleSize()); else throw new UserCancelled();
    }
