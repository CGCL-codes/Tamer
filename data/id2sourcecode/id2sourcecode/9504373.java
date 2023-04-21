    private void setAudioFileInfo(String audioFile) {
        if (audioFile == null || audioFile.equals("")) {
            audioFileName.setText("Choose an Audio File");
            clearAudioInfo();
            return;
        }
        File soundFile = BlueSystem.findFile(audioFile);
        if (soundFile == null || !soundFile.exists() || !soundFile.isFile()) {
            audioFileName.setText("Could not find file: " + audioFile);
            clearAudioInfo();
            return;
        }
        audioFileName.setText(audioFile);
        try {
            AudioFileFormat aFormat = AudioSystem.getAudioFileFormat(soundFile);
            AudioFormat format = aFormat.getFormat();
            durationText.setText(getDuration(aFormat, format));
            formatTypeText.setText(aFormat.getType().toString());
            byteLengthText.setText(Integer.toString(aFormat.getByteLength()));
            encodingTypeText.setText(format.getEncoding().toString());
            sampleRateText.setText(Float.toString(format.getSampleRate()));
            sampleSizeInBitsText.setText(Integer.toString(format.getSampleSizeInBits()));
            int numChannels = format.getChannels();
            channelsText.setText(Integer.toString(numChannels));
            setChannelVariablesInfo(numChannels);
            isBigEndianText.setText(getBooleanString(format.isBigEndian()));
        } catch (java.io.IOException ioe) {
            JOptionPane.showMessageDialog(null, BlueSystem.getString("soundfile.infoPanel.error.couldNotOpenFile") + " " + soundFile.getAbsolutePath());
            clearAudioInfo();
            return;
        } catch (javax.sound.sampled.UnsupportedAudioFileException uae) {
            JOptionPane.showMessageDialog(null, BlueSystem.getString("soundfile.infoPanel.error.unsupportedAudio") + " " + uae.getLocalizedMessage());
            clearAudioInfo();
            return;
        }
    }
