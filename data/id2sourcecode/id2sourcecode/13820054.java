    URLAudio(String urlstr) {
        try {
            URL url = new URL(urlstr);
            audioInputStream = AudioSystem.getAudioInputStream(url);
            frameLength = audioInputStream.getFrameLength();
            if (audioInputStream.markSupported()) audioInputStream.mark((int) frameLength);
            isBigEndian = audioInputStream.getFormat().isBigEndian();
            bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            channels = audioInputStream.getFormat().getChannels();
            sampleRate = audioInputStream.getFormat().getSampleRate();
            sampleSize = audioInputStream.getFormat().getSampleSizeInBits();
            bytesPerSample = sampleSize / 8;
            samplesPerFrame = bytesPerFrame / bytesPerSample;
            sampleLength = (frameLength / channels) * samplesPerFrame;
            audioStreamDescription = urlstr + " :\n " + Integer.toString(sampleSize) + " bits " + Float.toString(sampleRate) + " Hz " + Integer.toString(channels) + " channels " + Long.toString(sampleLength) + " samples/channel " + Float.toString((float) sampleLength / (float) sampleRate) + " seconds.";
        } catch (MalformedURLException ex) {
            System.out.println(ex.getMessage());
        } catch (UnsupportedAudioFileException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
