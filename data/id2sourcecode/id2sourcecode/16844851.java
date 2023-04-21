    @Override
    public Format[] getSupportedOutputFormats(Format input) {
        if (input == null) {
            return new Format[] { new AudioFormat(AudioFormat.LINEAR) };
        }
        final javax.sound.sampled.AudioFormat javaSoundFormat = JavaSoundUtils.convertFormat((AudioFormat) input);
        final javax.sound.sampled.AudioFormat[] targets1 = AudioSystem.getTargetFormats(Encoding.PCM_UNSIGNED, javaSoundFormat);
        final javax.sound.sampled.AudioFormat[] targets2 = AudioSystem.getTargetFormats(Encoding.PCM_SIGNED, javaSoundFormat);
        final javax.sound.sampled.AudioFormat[] targetsSpecial;
        Class classVorbisAudioFormat = null;
        Class classMpegAudioFormatt = null;
        if (!JavaSoundUtils.onlyStandardFormats) {
            try {
                classMpegAudioFormatt = Class.forName("javazoom.spi.mpeg.sampled.file.MpegAudioFormat");
                classVorbisAudioFormat = Class.forName("javazoom.spi.vorbis.sampled.file.VorbisAudioFormat");
            } catch (Exception dontcare) {
            }
        }
        if ((null != classMpegAudioFormatt) && classMpegAudioFormatt.isInstance(javaSoundFormat)) {
            javax.sound.sampled.AudioFormat decodedFormat = new javax.sound.sampled.AudioFormat(javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED, javaSoundFormat.getSampleRate(), 16, javaSoundFormat.getChannels(), javaSoundFormat.getChannels() * 2, javaSoundFormat.getSampleRate(), false);
            targetsSpecial = new javax.sound.sampled.AudioFormat[] { decodedFormat };
        } else if ((null != classVorbisAudioFormat) && classVorbisAudioFormat.isInstance(javaSoundFormat)) {
            javax.sound.sampled.AudioFormat decodedFormat = new javax.sound.sampled.AudioFormat(javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED, javaSoundFormat.getSampleRate(), 16, javaSoundFormat.getChannels(), javaSoundFormat.getChannels() * 2, javaSoundFormat.getSampleRate(), false);
            targetsSpecial = new javax.sound.sampled.AudioFormat[] { decodedFormat };
        } else {
            targetsSpecial = new javax.sound.sampled.AudioFormat[0];
        }
        final Format[] result = new Format[targets1.length + targets2.length + targetsSpecial.length];
        for (int i = 0; i < targets1.length; ++i) {
            result[i] = JavaSoundUtils.convertFormat(targets1[i]);
            logger.finer("getSupportedOutputFormats: " + result[i]);
        }
        for (int i = 0; i < targets2.length; ++i) {
            result[targets1.length + i] = JavaSoundUtils.convertFormat(targets2[i]);
            logger.finer("getSupportedOutputFormats: " + result[targets1.length + i]);
        }
        for (int i = 0; i < targetsSpecial.length; ++i) {
            result[targets1.length + targets2.length + i] = JavaSoundUtils.convertFormat(targetsSpecial[i]);
            logger.finer("getSupportedOutputFormats: " + result[targets1.length + targets2.length + i]);
        }
        for (int i = 0; i < result.length; ++i) {
            AudioFormat a = ((AudioFormat) result[i]);
            AudioFormat inputAudioFormat = (AudioFormat) input;
            if (FormatUtils.specified(inputAudioFormat.getSampleRate()) && !FormatUtils.specified(a.getSampleRate())) result[i] = null;
        }
        return result;
    }
