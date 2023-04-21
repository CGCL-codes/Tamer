    protected void processPlay(int modifiers) {
        log.log(Level.INFO, "processPlay....... ");
        if (playlist.isModified()) {
            PlayListItem pli = playlist.getCursor();
            log.log(Level.INFO, "播放列表改了..." + pli);
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
                player.resume();
            } catch (BasicPlayerException e) {
            }
            playerState = PLAY;
        } else if (playerState == PLAY) {
            try {
                player.stop();
            } catch (BasicPlayerException e) {
            }
            playerState = PLAY;
            secondsAmount = 0;
            timePanel.reset();
            if (currentFileOrURL != null) {
                try {
                    if (currentIsFile == true) {
                        player.open(new File(currentFileOrURL));
                    } else {
                        player.open(new URL(currentFileOrURL));
                    }
                    player.play();
                } catch (Exception ex) {
                    showMessage(Config.getResource("title.invalidfile"));
                }
            }
        } else if ((playerState == STOP) || (playerState == OPEN)) {
            try {
                player.stop();
            } catch (BasicPlayerException e) {
            }
            if (currentFileOrURL != null) {
                try {
                    if (currentIsFile == true) {
                        player.open(new File(currentFileOrURL));
                    } else {
                        player.open(new URL(currentFileOrURL));
                    }
                    player.play();
                    lyric = new Lyric(currentItem);
                    lyricUI.setLyric(lyric);
                    titleText = currentSongName.toUpperCase();
                    int bitRate = -1;
                    if (currentItem != null) {
                        bitRate = currentItem.getBitrate();
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
                    if (currentItem != null) {
                        channels = currentItem.getChannels();
                    }
                    if ((channels <= 0) && (audioInfo.containsKey("audio.channels"))) {
                        channels = ((Integer) audioInfo.get("audio.channels")).intValue();
                    }
                    float sampleRate = -1.0f;
                    if (currentItem != null) {
                        sampleRate = currentItem.getSamplerate();
                    }
                    if ((sampleRate <= 0) && (audioInfo.containsKey("audio.samplerate.hz"))) {
                        sampleRate = ((Float) audioInfo.get("audio.samplerate.hz")).floatValue();
                    }
                    long lenghtInSecond = -1L;
                    if (currentItem != null) {
                        lenghtInSecond = currentItem.getLength();
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
                    currentItem.setSampled(String.valueOf(Math.round((sampleRate / 1000))) + "kHz");
                    if (bitRate > 999) {
                        bitRate = (bitRate / 100);
                        currentItem.setBitRate(bitRate + "Hkbps");
                    } else {
                        currentItem.setBitRate(String.valueOf(bitRate) + "kbps");
                    }
                    if (channels == 2) {
                        currentItem.setChannels(Config.getResource("songinfo.channel.stereo"));
                    } else if (channels == 1) {
                        currentItem.setChannels(Config.getResource("songinfo.channel.mono"));
                    }
                } catch (BasicPlayerException bpe) {
                    showMessage(Config.getResource("title.invalidfile"));
                } catch (MalformedURLException mue) {
                    showMessage(Config.getResource("title.invalidfile"));
                }
                try {
                    if (config.isMute()) {
                        player.setGain(0);
                    } else {
                        player.setGain(((double) volume.getValue() / (double) volume.getMaximum()));
                    }
                    player.setPan((float) pan.getValue() / 10.0f);
                } catch (BasicPlayerException e) {
                }
                playerState = PLAY;
            }
        }
        changePlayPauseState(playerState);
        pos.setEnabled(true);
        if (!isSeeked && config.isAutoPlayWhenStart() && config.isMaintainLastPlay()) {
            isSeeked = true;
            processSeek(lastRate);
        }
    }
