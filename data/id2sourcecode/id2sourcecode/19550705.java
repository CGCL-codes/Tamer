    protected void processPlay(int modifiers) {
        if (playlist.isModified()) {
            PlaylistItem pli = playlist.getCursor();
            if (pli == null) {
                playlist.begin();
                pli = playlist.getCursor();
            }
            setCurrentSong(pli);
            playlist.setModified(false);
            playlistUI.repaint();
        }
        if (playerState == PAUSE) {
            try {
                theSoundPlayer.resume();
            } catch (BasicPlayerException e) {
                log.error("Cannot resume", e);
            }
            playerState = PLAY;
            ui.getAcPlayIcon().setIcon(0);
            ui.getAcTimeIcon().setIcon(0);
        } else if (playerState == PLAY) {
            try {
                theSoundPlayer.stop();
            } catch (BasicPlayerException e) {
                log.error("Cannot stop", e);
            }
            playerState = PLAY;
            secondsAmount = 0;
            ui.getAcMinuteH().setAcText("0");
            ui.getAcMinuteL().setAcText("0");
            ui.getAcSecondH().setAcText("0");
            ui.getAcSecondL().setAcText("0");
            if (currentFileOrURL != null) {
                try {
                    if (currentIsFile == true) {
                        theSoundPlayer.open(openFile(currentFileOrURL));
                    } else {
                        theSoundPlayer.open(new URL(currentFileOrURL));
                    }
                    theSoundPlayer.play();
                } catch (Exception ex) {
                    log.error("Cannot read file : " + currentFileOrURL, ex);
                    if (ex instanceof ConnectException) {
                        showMessage(ui.getResource("title.connect.error"));
                    } else {
                        showMessage(ui.getResource("title.invalidfile"));
                    }
                }
            }
        } else if ((playerState == STOP) || (playerState == OPEN)) {
            try {
                theSoundPlayer.stop();
            } catch (BasicPlayerException e) {
                log.error("Stop failed", e);
            }
            if (currentFileOrURL != null) {
                try {
                    if (currentIsFile == true) {
                        theSoundPlayer.open(openFile(currentFileOrURL));
                    } else {
                        theSoundPlayer.open(new URL(currentFileOrURL));
                    }
                    theSoundPlayer.play();
                    titleText = currentSongName.toUpperCase();
                    int bitRate = -1;
                    if (currentPlaylistItem != null) {
                        bitRate = currentPlaylistItem.getBitrate();
                    }
                    if ((bitRate <= 0) && (audioInfo.containsKey("bitrate"))) {
                        bitRate = ((Integer) audioInfo.get("bitrate")).intValue();
                    }
                    if ((bitRate <= 0) && (audioInfo.containsKey("audio.framerate.fps")) && (audioInfo.containsKey("audio.framesize.bytes"))) {
                        float FR = ((Float) audioInfo.get("audio.framerate.fps")).floatValue();
                        int FS = ((Integer) audioInfo.get("audio.framesize.bytes")).intValue();
                        bitRate = Math.round(FS * FR * 8);
                    }
                    int channels = -1;
                    if (currentPlaylistItem != null) {
                        channels = currentPlaylistItem.getChannels();
                    }
                    if ((channels <= 0) && (audioInfo.containsKey("audio.channels"))) {
                        channels = ((Integer) audioInfo.get("audio.channels")).intValue();
                    }
                    float sampleRate = -1.0f;
                    if (currentPlaylistItem != null) {
                        sampleRate = currentPlaylistItem.getSamplerate();
                    }
                    if ((sampleRate <= 0) && (audioInfo.containsKey("audio.samplerate.hz"))) {
                        sampleRate = ((Float) audioInfo.get("audio.samplerate.hz")).floatValue();
                    }
                    long lenghtInSecond = -1L;
                    if (currentPlaylistItem != null) {
                        lenghtInSecond = currentPlaylistItem.getLength();
                    }
                    if ((lenghtInSecond <= 0) && (audioInfo.containsKey("duration"))) {
                        lenghtInSecond = ((Long) audioInfo.get("duration")).longValue() / 1000000;
                    }
                    if ((lenghtInSecond <= 0) && (audioInfo.containsKey("audio.length.bytes"))) {
                        lenghtInSecond = (long) Math.round(getTimeLengthEstimation(audioInfo) / 1000);
                        if (lenghtInSecond > 0) {
                            int minutes = (int) Math.floor(lenghtInSecond / 60);
                            int hours = (int) Math.floor(minutes / 60);
                            minutes = minutes - hours * 60;
                            int seconds = (int) (lenghtInSecond - minutes * 60 - hours * 3600);
                            if (seconds >= 10) {
                                titleText = "(" + minutes + ":" + seconds + ") " + titleText;
                            } else {
                                titleText = "(" + minutes + ":0" + seconds + ") " + titleText;
                            }
                        }
                    }
                    bitRate = Math.round((bitRate / 1000));
                    ui.getAcSampleRateLabel().setAcText(String.valueOf(Math.round((sampleRate / 1000))));
                    if (bitRate > 999) {
                        bitRate = (int) (bitRate / 100);
                        ui.getAcBitRateLabel().setAcText(bitRate + "H");
                    } else {
                        ui.getAcBitRateLabel().setAcText(String.valueOf(bitRate));
                    }
                    if (channels == 2) {
                        ui.getAcStereoIcon().setIcon(1);
                        ui.getAcMonoIcon().setIcon(0);
                    } else if (channels == 1) {
                        ui.getAcStereoIcon().setIcon(0);
                        ui.getAcMonoIcon().setIcon(1);
                    }
                    showTitle(titleText);
                    ui.getAcMinuteH().setAcText("0");
                    ui.getAcMinuteL().setAcText("0");
                    ui.getAcSecondH().setAcText("0");
                    ui.getAcSecondL().setAcText("0");
                    ui.getAcPlayIcon().setIcon(0);
                    ui.getAcTimeIcon().setIcon(0);
                    dInfo = new DetailInfo(bitRate, (int) sampleRate, channels);
                } catch (BasicPlayerException bpe) {
                    log.error("Stream error :" + currentFileOrURL, bpe);
                    showMessage(ui.getResource("title.invalidfile"));
                } catch (MalformedURLException mue) {
                    log.error("Stream error :" + currentFileOrURL, mue);
                    showMessage(ui.getResource("title.invalidfile"));
                }
                try {
                    theSoundPlayer.setGain(((double) ui.getAcVolume().getValue() / (double) ui.getAcVolume().getMaximum()));
                    theSoundPlayer.setPan((float) ui.getAcBalance().getValue() / 10.0f);
                } catch (BasicPlayerException e) {
                    log.error("Cannot set control", e);
                }
                playerState = PLAY;
                log.info(titleText);
            }
        }
    }
