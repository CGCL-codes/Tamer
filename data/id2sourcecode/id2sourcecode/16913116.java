    public static void playSound(final Sound sound) {
        if (sounds.size() == 0) {
            for (Sound s : Sound.values()) {
                AudioInputStream stream = null;
                Clip clip = null;
                try {
                    switch(s) {
                        case ALERT:
                            stream = AudioSystem.getAudioInputStream(new File(Utils.getSoundsDirectory() + "alert.wav"));
                            break;
                        case LOGIN:
                            stream = AudioSystem.getAudioInputStream(new File(Utils.getSoundsDirectory() + "login.wav"));
                            break;
                        case LOGOUT:
                            stream = AudioSystem.getAudioInputStream(new File(Utils.getSoundsDirectory() + "logout.wav"));
                            break;
                        case RECEIVE:
                            stream = AudioSystem.getAudioInputStream(new File(Utils.getSoundsDirectory() + "receive.wav"));
                            break;
                        case SEND:
                            stream = AudioSystem.getAudioInputStream(new File(Utils.getSoundsDirectory() + "send.wav"));
                            break;
                        default:
                            throw new java.lang.IllegalArgumentException("Missing case " + sound);
                    }
                    AudioFormat format = stream.getFormat();
                    if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                        stream = AudioSystem.getAudioInputStream(format, stream);
                    }
                    DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
                    clip = (Clip) AudioSystem.getLine(info);
                    clip.open(stream);
                    clip.drain();
                    sounds.put(s, clip);
                } catch (MalformedURLException e) {
                    log.error(null, e);
                } catch (IOException e) {
                    log.error(null, e);
                } catch (LineUnavailableException e) {
                    log.error(null, e);
                } catch (UnsupportedAudioFileException e) {
                    log.error(null, e);
                } finally {
                }
            }
        }
        soundPool.execute(new Runnable() {

            @Override
            public void run() {
                Clip clip = sounds.get(sound);
                if (clip == null) {
                    return;
                }
                clip.stop();
                clip.flush();
                clip.setFramePosition(0);
                clip.loop(0);
            }
        });
    }
