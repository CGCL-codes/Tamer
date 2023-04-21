    public void controllerUpdate(ControllerEvent ce) {
        if (ce instanceof AudioDeviceUnavailableEvent) {
        } else if (ce instanceof CachingControlEvent) {
        } else if (ce instanceof ControllerClosedEvent) {
            if (ce instanceof ControllerErrorEvent) {
                if (ce instanceof ConnectionErrorEvent) {
                } else if (ce instanceof InternalErrorEvent) {
                } else if (ce instanceof ResourceUnavailableEvent) {
                }
            } else if (ce instanceof DataLostErrorEvent) {
            }
        } else if (ce instanceof DurationUpdateEvent) {
            totalSeconds = (int) Math.round(player.getDuration().getSeconds());
            System.out.println("New media duration is " + totalSeconds);
            sendSongState(null);
        } else if (ce instanceof FormatChangeEvent) {
            if (ce instanceof SizeChangeEvent) {
            }
        } else if (ce instanceof MediaTimeSetEvent) {
            playedSeconds = (int) Math.round(player.getMediaTime().getSeconds());
            System.out.println("New media time is " + playedSeconds);
            sendPlayedSeconds(null);
        } else if (ce instanceof RateChangeEvent) {
        } else if (ce instanceof StopTimeChangeEvent) {
        } else if (ce instanceof TransitionEvent) {
            if (ce instanceof ConfigureCompleteEvent) {
            } else if (ce instanceof PrefetchCompleteEvent) {
            } else if (ce instanceof RealizeCompleteEvent) {
            } else if (ce instanceof StartEvent) {
                System.out.println("Player has started");
                playState = PS_PLAYING;
                totalSeconds = (int) Math.round(player.getDuration().getSeconds());
                System.out.println("New media duration is " + totalSeconds);
                FormatControl fc = (FormatControl) player.getControl("javax.media.control.FormatControl");
                Format format = fc.getFormat();
                System.out.println("Format is " + format);
                String encoding = format.getEncoding();
                System.out.println("  Encoding is " + encoding);
                if (format instanceof AudioFormat) {
                    System.out.println("  Format is an AudioFormat");
                    String mp3EncodingRate = new String();
                    if (encoding.equals("mpeglayer3")) {
                        mp3EncodingRate = new Double(((AudioFormat) format).getFrameRate() * 8 / 1000).toString();
                        System.out.println("    MP3 encoding Rate is " + mp3EncodingRate + " kbps");
                    }
                    String sampleRate = new Double(((AudioFormat) format).getSampleRate()).toString();
                    System.out.println("    Sample Rate is " + sampleRate);
                    String sampleSize = new Integer(((AudioFormat) format).getSampleSizeInBits()).toString();
                    System.out.println("    Sample Size is " + sampleSize);
                    String numberOfChannels = new Integer(((AudioFormat) format).getChannels()).toString();
                    System.out.println("    # of channels is " + numberOfChannels);
                    currentSongInfo = "Bitrate: " + mp3EncodingRate + "  Freq: " + sampleRate + "  Channels: " + numberOfChannels;
                } else {
                    System.out.println("  Format is not an AudioFormat");
                }
                if (encoding.equals("mpeglayer3")) {
                    ID3 id3 = new ID3(songFile);
                    try {
                        currentArtist = id3.getArtist();
                        currentAlbum = id3.getAlbum();
                        currentSong = id3.getTitle();
                    } catch (NoID3TagException e) {
                        currentArtist = "";
                        currentAlbum = "";
                        currentSong = songFile.getName();
                    }
                }
                sendSongState(null);
            } else if (ce instanceof StopEvent) {
                if (ce instanceof DataStarvedEvent) {
                    System.out.println("Player is starved for data");
                } else if (ce instanceof DeallocateEvent) {
                    System.out.println("Player is being deallocated");
                    switchToStoppedPlayState();
                } else if (ce instanceof EndOfMediaEvent) {
                    System.out.println("End of song reached, player stopped");
                    if (currentPlaylistIndex < workingPlaylist.size() - 1) {
                        System.out.println("  Starting next song in playlist");
                        play(currentPlaylistIndex + 1);
                    } else if (currentPlaylistIndex == workingPlaylist.size() - 1 && loopEnabled) {
                        System.out.println("  Looping back to first song in " + "playlist");
                        play(0);
                    }
                } else if (ce instanceof RestartingEvent) {
                    System.out.println("Player is restarting");
                } else if (ce instanceof StopAtTimeEvent) {
                    System.out.println("Player stopped due to a " + "StopAtTimeEvent");
                    switchToStoppedPlayState();
                    sendSongState(null);
                } else if (ce instanceof StopByRequestEvent) {
                    System.out.println("Player stopped on request");
                    if (requestedPause) {
                        System.out.println("  Player paused");
                        playState = PS_PAUSED;
                        requestedPause = false;
                    } else {
                        System.out.println("  Player stopped");
                        switchToStoppedPlayState();
                    }
                    sendSongState(null);
                } else {
                    System.out.println("Player stopped for an unknown reason");
                    switchToStoppedPlayState();
                    sendSongState(null);
                }
            }
        } else {
            System.out.println("Unknown ControllerEvent:  " + ce.toString());
        }
    }
