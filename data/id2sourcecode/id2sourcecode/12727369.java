    public void processIncomingMessages() {
        if (incomingMessages.size() > 0) {
            pingCount = 0;
            Iterator<Message> iter = incomingMessages.iterator();
            while (iter.hasNext()) {
                if (incomingMessages.size() < 2) logger.Debug("(Processing messages) There is now " + incomingMessages.size() + " incoming message."); else logger.Debug("(Processing messages) There are now " + incomingMessages.size() + " incoming messages.");
                try {
                    Message msg = iter.next();
                    Integer showId;
                    String dataStr;
                    logger.Debug("(Processing messages) " + msg.toString());
                    switch(msg.getType()) {
                        case DELETE_SHOW:
                            showId = Integer.valueOf(msg.getData());
                            boolean successful = deleteShow(showId);
                            if (successful) {
                                send(new Message(MessageType.DELETE_SHOW, "OK"));
                            } else send(new Message(MessageType.DELETE_SHOW, "BAD"));
                            break;
                        case ALL_CHANNELS:
                            dataStr = msg.getData();
                            if (dataStr.isEmpty() || dataStr.equalsIgnoreCase("All")) getAllChannels(); else getChannelsOnLineup(dataStr);
                            break;
                        case AIRINGS_ON_CHANNEL_AT_TIME:
                            getAiringsOnChannelAtTime(msg.getData());
                            break;
                        case ANSWER:
                            this.isXML = msg.getData().equalsIgnoreCase("XML");
                            this.isJSON2 = msg.getData().equalsIgnoreCase("JSON2");
                            break;
                        case BUTTON:
                            getButtons();
                            break;
                        case CHANGE_FAVORITE_FREQUENCY:
                            changeFavoriteFrequency(msg.getData());
                            break;
                        case DELETE_FAVORITE_SHOW:
                            deleteFavoriteShow(msg.getData());
                            break;
                        case EXECUTE:
                            Runtime.getRuntime().exec(msg.getData());
                            break;
                        case MAXIMUM_ITEMS:
                            try {
                                maxItems = Integer.valueOf(msg.getData());
                                send(new Message(MessageType.MAXIMUM_ITEMS, "OK"));
                            } catch (Exception e) {
                                logger.Debug(e.getMessage());
                                send(new Message(MessageType.MAXIMUM_ITEMS, "BAD"));
                            }
                            break;
                        case ALL_THE_LINEUPS:
                            dataStr = getLineups();
                            send(new Message(MessageType.ALL_THE_LINEUPS, dataStr));
                            break;
                        case RESET_SYSTEM_MESSAGE:
                            sageApi.systemMessageAPI.DeleteAllSystemMessages();
                            this.lastSM = 0l;
                            break;
                        case RESET:
                            resetLists();
                            this.lastSM = 0l;
                        case INITIALIZE:
                            if (StartServers.Password.isEmpty() || StartServers.Password.equals(msg.getData())) {
                                isInitialized = true;
                                sendAvailableData();
                                send(new Message(MessageType.PROTOCOL_STREAMING_TYPE, StreamingType));
                                send(new Message(MessageType.STREAMING_PORT, String.valueOf(StreamingPort)));
                                send(new Message(MessageType.INITIALIZE, "OK"));
                                dataStr = sageApi.configuration.GetProperty("jetty/jetty.port", null);
                                if (dataStr != null && !dataStr.isEmpty()) send(new Message(MessageType.JETTY_HTTP_PORT, dataStr));
                                initialFavoriteList();
                                initialManualRecordingsList();
                                initialSystemMessages();
                                initialTVFileList();
                                initialUpcomingRecordingsList();
                            } else shutdown();
                            break;
                        case UPCOMING_EPISODES_LIST:
                            dataStr = getUpcomingEpisodesList(msg.getData());
                            if (!dataStr.isEmpty()) send(new Message(MessageType.UPCOMING_EPISODES_LIST, dataStr));
                            break;
                        case LIST_OF_CLIENTS:
                            getClients();
                            break;
                        case MUSIC_FILES_LIST:
                            dataStr = msg.getData();
                            if (!dataStr.isEmpty()) {
                                if (dataStr.equalsIgnoreCase("Reset")) offsetAlbum = 0; else offsetAlbum = Integer.valueOf(dataStr);
                            }
                            getAlbumList();
                            break;
                        case OTHER_FILES_LIST:
                            dataStr = msg.getData();
                            if (!dataStr.isEmpty()) {
                                if (dataStr.equalsIgnoreCase("Reset")) {
                                    offsetOther = 0;
                                    this.lastVideo = 0l;
                                } else {
                                    String SEPARATOR_CHAR = "\\|";
                                    String[] parameters = dataStr.split(SEPARATOR_CHAR);
                                    this.lastVideo = Long.valueOf(parameters[0]);
                                    offsetOther = Integer.valueOf(parameters[1]);
                                }
                            }
                            getMediaFileList("IsLibraryFile", MessageType.OTHER_FILES_LIST);
                            break;
                        case PICTURE_FILES_LIST:
                            dataStr = msg.getData();
                            if (!dataStr.isEmpty()) {
                                if (dataStr.equalsIgnoreCase("Reset")) {
                                    offsetPicture = 0;
                                    this.lastPhoto = 0l;
                                } else {
                                    String SEPARATOR_CHAR = "\\|";
                                    String[] parameters = dataStr.split(SEPARATOR_CHAR);
                                    this.lastPhoto = Long.valueOf(parameters[0]);
                                    offsetPicture = Integer.valueOf(parameters[1]);
                                }
                            }
                            getMediaFileList("IsPictureFile", MessageType.PICTURE_FILES_LIST);
                            break;
                        case RECORD_A_SHOW:
                            recordAShow(msg.getData());
                            break;
                        case SEARCH_BY_TITLE:
                            dataStr = searchByTitle(msg.getData());
                            if (!dataStr.isEmpty()) send(new Message(MessageType.SEARCH_BY_TITLE, dataStr));
                            break;
                        case MATCH_EXACT_TITLE:
                            dataStr = searchByExactTitle(msg.getData());
                            if (!dataStr.isEmpty()) send(new Message(MessageType.MATCH_EXACT_TITLE, dataStr));
                            break;
                        case LAST_EPG_DOWNLOAD:
                            send(new Message(MessageType.LAST_EPG_DOWNLOAD, String.valueOf(sageApi.global.GetLastEPGDownloadTime() / 100000)));
                            break;
                        case NEXT_EPG_DOWNLOAD:
                            send(new Message(MessageType.NEXT_EPG_DOWNLOAD, String.valueOf(sageApi.global.GetTimeUntilNextEPGDownload() / 1000)));
                            break;
                        case STREAM_VLC_ALBUM:
                            showId = Integer.valueOf(msg.getData());
                            setVLCAlbum(showId);
                            break;
                        case STREAM_VLC:
                            showId = Integer.valueOf(msg.getData());
                            setVLC(showId);
                            break;
                        case STREAM_VLC_PROFILE:
                            StreamingProfile = Integer.valueOf(msg.getData());
                            break;
                        case SYSTEM_MESSAGE:
                            dataStr = msg.getData();
                            if (!dataStr.isEmpty()) {
                                this.lastSM = Long.valueOf(dataStr);
                                if (isInitialized) initialSystemMessages();
                            }
                            break;
                        default:
                            logger.Debug("Default case reached.  Didn't know what to do with: " + msg.getData());
                            break;
                    }
                } catch (Throwable t) {
                    logger.Error(t);
                }
                iter.remove();
            }
        }
    }
