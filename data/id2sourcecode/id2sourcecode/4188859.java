    private String getChannelVariables() {
        int numChannels;
        try {
            numChannels = SoundFileUtilities.getNumberOfChannels(getSoundFileName());
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, BlueSystem.getString("soundfile.infoPanel.error.couldNotOpenFile") + " " + getSoundFileName());
            return null;
        } catch (javax.sound.sampled.UnsupportedAudioFileException uae) {
            JOptionPane.showMessageDialog(null, BlueSystem.getString("soundfile.infoPanel.error.unsupportedAudio") + " " + uae.getLocalizedMessage());
            return null;
        }
        if (numChannels <= 0) {
            return null;
        }
        String info = "aChannel1";
        int i = 1;
        while (i < numChannels) {
            i++;
            info += ", aChannel" + i;
        }
        return info;
    }
