    public void setSoundFile(File soundFile) {
        Object oldSoundFile = this.soundFile;
        this.soundFile = soundFile;
        boolean isAudioFile = true;
        AudioFileFormat aFormat = null;
        AudioFormat format = null;
        try {
            aFormat = AudioSystem.getAudioFileFormat(soundFile);
            format = aFormat.getFormat();
        } catch (Exception e) {
            isAudioFile = false;
            timeDivisor = -1;
        }
        stop();
        if (this.soundFile == null || !isAudioFile) {
            this.fileNameText.setText("");
            this.playStopButton.setEnabled(false);
            playStopButton.setText(BlueSystem.getString("soundfile.player.noFileSelected"));
        } else {
            this.fileNameText.setText(soundFile.getAbsolutePath());
            this.playStopButton.setEnabled(true);
            playStopButton.setText(BlueSystem.getString("soundfile.player.playStop"));
            timeDivisor = format.getSampleRate() * (format.getSampleSizeInBits() / 8) * format.getChannels();
            currentTime = "00:00:00";
            duration = getTimeString(aFormat.getByteLength());
            timeDisplay.setText(currentTime + "/" + duration);
        }
        firePropertyChange("soundFile", oldSoundFile, this.soundFile);
    }
