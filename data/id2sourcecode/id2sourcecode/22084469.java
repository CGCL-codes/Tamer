    public void setAudioFormat(AudioFormat f) {
        comboAudioEncoding.setSelectedItem(f.getEncoding());
        comboAudioSampleRate.setEnabled(false);
        radioAudioChannelsMono.setEnabled(false);
        radioAudioChannelsStereo.setEnabled(false);
        radioAudioEndianLittle.setEnabled(false);
        radioAudioEndianBig.setEnabled(false);
        radioAudioBitsPerSample8.setEnabled(false);
        radioAudioBitsPerSample16.setEnabled(false);
        checkBoxAudioSigned.setEnabled(false);
        comboAudioSampleRate.setSelectedItem("" + (int) f.getSampleRate());
        if (f.getChannels() == 1) radioAudioChannelsMono.setSelected(true); else if (f.getChannels() == 2) radioAudioChannelsStereo.setSelected(true); else throw new IllegalArgumentException();
        if (f.getEndian() == AudioFormat.LITTLE_ENDIAN) radioAudioEndianLittle.setSelected(true); else if (f.getEndian() == AudioFormat.BIG_ENDIAN) radioAudioEndianBig.setSelected(true); else {
            if (f.getSampleSizeInBits() > 8) throw new IllegalArgumentException("Unknown or unspecified endian: " + f.getEndian() + " format: " + f);
            radioAudioEndianLittle.setSelected(false);
            radioAudioEndianBig.setSelected(false);
        }
        if (f.getSampleSizeInBits() == 8) radioAudioBitsPerSample8.setSelected(true); else if (f.getSampleSizeInBits() == 16) radioAudioBitsPerSample16.setSelected(true); else throw new IllegalArgumentException();
        if (f.getSigned() == AudioFormat.SIGNED) checkBoxAudioSigned.setSelected(true); else if (f.getSigned() == AudioFormat.UNSIGNED) checkBoxAudioSigned.setSelected(false); else throw new IllegalArgumentException();
    }
