        public UserInfoHolder(AudioFormat selectedFormat, long time) {
            content = "audio";
            packageName = "package=javax.sound.sampled";
            encoding = selectedFormat.getEncoding();
            channels = selectedFormat.getChannels();
            sampleRate = selectedFormat.getSampleRate();
            sampleSize = selectedFormat.getSampleSizeInBits();
            frameRate = selectedFormat.getFrameRate();
            frameSize = selectedFormat.getFrameSize();
            bigEndian = selectedFormat.isBigEndian();
            endian = (selectedFormat.isBigEndian()) ? -1 : 0;
            signed = 0;
            if (selectedFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) signed = -1;
            startAt = time;
        }
