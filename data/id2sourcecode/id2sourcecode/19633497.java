    public void play(NetContext context, String streamName, int start, int len) throws NetConnectionException, IOException, FlvException {
        if (player != null) {
            writeErrorMessage("This channel is already playing");
            return;
        }
        this.streamName = streamName;
        rtmp.writeProtocolControlMessage(new RtmpMessageChunkSize(1024));
        writeStatusMessage("NetStream.Play.Reset", AmfValue.newObject().put("description", "Resetting " + streamName + ".").put("details", streamName).put("clientId", streamId));
        rtmp.writeProtocolControlMessage(new RtmpMessageUserControl(RtmpMessageUserControl.EVT_STREAM_IS_RECORDED, streamId));
        rtmp.writeProtocolControlMessage(new RtmpMessageUserControl(RtmpMessageUserControl.EVT_STREAM_BEGIN, streamId));
        writeStatusMessage("NetStream.Play.Start", AmfValue.newObject().put("description", "Start playing " + streamName + ".").put("clientId", streamId));
        String app = context.getAttribute("app");
        switch(start) {
            case -1:
                {
                    StreamPublisher publisher = (StreamPublisher) PublisherManager.getPublisher(streamName);
                    if (publisher == null) {
                        writeErrorMessage("Unknown shared stream '" + streamName + "'");
                        return;
                    }
                    StreamSubscriber subscriber = new StreamSubscriber(publisher, this);
                    publisher.addSubscriber(subscriber);
                    subscriber.start();
                }
                break;
            case -2:
                {
                    StreamPublisher publisher = (StreamPublisher) PublisherManager.getPublisher(streamName);
                    if (publisher != null) {
                        StreamSubscriber subscriber = new StreamSubscriber(publisher, this);
                        publisher.addSubscriber(subscriber);
                        subscriber.start();
                    } else {
                        String tokens[] = streamName.split(":");
                        String type = "";
                        String file = streamName;
                        if (tokens.length >= 2) {
                            type = tokens[0];
                            file = tokens[1];
                        }
                        String path = context.getRealPath(app, file, type);
                        player = createPlayer(type, path);
                        player.seek(0);
                    }
                }
                break;
            default:
                String tokens[] = streamName.split(":");
                String type = "";
                String file = streamName;
                if (tokens.length >= 2) {
                    type = tokens[0];
                    file = tokens[1];
                }
                String path = context.getRealPath(app, file, type);
                player = createPlayer(type, path);
                player.seek(start);
        }
    }
