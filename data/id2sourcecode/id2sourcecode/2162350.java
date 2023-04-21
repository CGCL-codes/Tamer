        public void run() {
            try {
                File file = new File(songPath);
                AudioInputStream in = AudioSystem.getAudioInputStream(file);
                setDin(null);
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                setDin(AudioSystem.getAudioInputStream(decodedFormat, in));
                rawplay(decodedFormat, getDin());
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
