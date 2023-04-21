    public Sounds(ResourceLocator rl) {
        for (SoundType st : SoundType.values()) {
            try {
                AudioInputStream ain = AudioSystem.getAudioInputStream(new ByteArrayInputStream(rl.get(st.resource, ResourceType.AUDIO).get()));
                try {
                    AudioFormat af = ain.getFormat();
                    byte[] snd = IOUtils.load(ain);
                    if (af.getSampleSizeInBits() == 8) {
                        if (af.getEncoding() == Encoding.PCM_UNSIGNED) {
                            for (int i = 0; i < snd.length; i++) {
                                snd[i] = (byte) ((snd[i] & 0xFF) - 128);
                            }
                        }
                        snd = AudioThread.convert8To16(snd);
                        af = new AudioFormat(af.getSampleRate(), 16, af.getChannels(), true, af.isBigEndian());
                    }
                    soundMap.put(st, snd);
                    soundFormat.put(st, af);
                } finally {
                    ain.close();
                }
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
