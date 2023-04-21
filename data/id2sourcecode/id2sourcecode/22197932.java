            @Override
            public void run() {
                alive = true;
                AudioInputStream audioInputStream = null;
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new File("tempSongFileOut.aac"));
                    try {
                        total = audioInputStream.available();
                    } catch (IOException e) {
                        LOG.log(Level.SEVERE, e.getMessage());
                        if (alive) {
                            Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                        }
                        return;
                    }
                } catch (UnsupportedAudioFileException e) {
                    LOG.log(Level.SEVERE, e.getMessage());
                    if (alive) {
                        Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                    }
                    return;
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, e.getMessage());
                    if (alive) {
                        Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                    }
                    return;
                }
                SourceDataLine sourceDataLine = null;
                try {
                    AudioFormat audioFormat = audioInputStream.getFormat();
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                    sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
                    sourceDataLine.open(audioFormat);
                    duration = audioInputStream.available() / audioFormat.getSampleRate() / (audioFormat.getSampleSizeInBits() / 8.0) / audioFormat.getChannels();
                } catch (LineUnavailableException e) {
                    LOG.log(Level.WARNING, e.getMessage());
                    if (alive) {
                        Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                    }
                    return;
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, e.getMessage());
                    if (alive) {
                        Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                    }
                }
                sourceDataLine.start();
                byte[] data = new byte[256];
                try {
                    int bytesRead = 0;
                    while (bytesRead != -1 && alive) {
                        while (paused) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                LOG.log(Level.SEVERE, e.getMessage());
                                if (alive) {
                                    Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                                }
                                return;
                            }
                        }
                        bytesRead = audioInputStream.read(data, 0, data.length);
                        updateProgressInformation(audioInputStream);
                        adjustVolumeIfNeeded(sourceDataLine);
                        if (bytesRead >= 0) sourceDataLine.write(data, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (alive) {
                        Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                    }
                    alive = false;
                    return;
                } finally {
                    sourceDataLine.drain();
                    sourceDataLine.close();
                    if (alive) {
                        Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                    }
                }
                if (alive) {
                    Pandora.pandoraPlayer.setPlayerState(PandoraPlayer.PlayerState.WAITING_FOR_NEXT_SONG);
                }
            }
