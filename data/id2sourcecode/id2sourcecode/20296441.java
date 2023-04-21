    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent me) {
        if (publisher != null && publisher.handle(me)) {
            return;
        }
        final Channel channel = me.getChannel();
        final RtmpMessage message = (RtmpMessage) me.getMessage();
        switch(message.getHeader().getMessageType()) {
            case CHUNK_SIZE:
                break;
            case CONTROL:
                Control control = (Control) message;
                logger.debug("control: {}", control);
                switch(control.getType()) {
                    case PING_REQUEST:
                        final int time = control.getTime();
                        logger.debug("server ping: {}", time);
                        Control pong = Control.pingResponse(time);
                        logger.debug("sending ping response: {}", pong);
                        channel.write(pong);
                        break;
                    case SWFV_REQUEST:
                        if (swfvBytes == null) {
                            logger.warn("swf verification not initialized!" + " not sending response, server likely to stop responding / disconnect");
                        } else {
                            Control swfv = Control.swfvResponse(swfvBytes);
                            logger.info("sending swf verification response: {}", swfv);
                            channel.write(swfv);
                        }
                        break;
                    case STREAM_BEGIN:
                        if (publisher != null && !publisher.isStarted()) {
                            publisher.start(channel, options.getStart(), options.getLength(), new ChunkSize(4096));
                            return;
                        }
                        if (streamId != 0) {
                            channel.write(Control.setBuffer(streamId, options.getBuffer()));
                        }
                        break;
                    default:
                        logger.debug("ignoring control message: {}", control);
                }
                break;
            case METADATA_AMF0:
            case METADATA_AMF3:
                Metadata metadata = (Metadata) message;
                if (metadata.getName().equals("onMetaData")) {
                    logger.debug("writing 'onMetaData': {}", metadata);
                    if (writer != null) writer.write(message);
                } else {
                    logger.debug("ignoring metadata: {}", metadata);
                }
                break;
            case AUDIO:
            case VIDEO:
            case AGGREGATE:
                if (writer != null) writer.write(message);
                bytesRead += message.getHeader().getSize();
                if ((bytesRead - bytesReadLastSent) > bytesReadWindow) {
                    logger.debug("sending bytes read ack {}", bytesRead);
                    bytesReadLastSent = bytesRead;
                    channel.write(new BytesRead(bytesRead));
                }
                break;
            case COMMAND_AMF0:
            case COMMAND_AMF3:
                Command command = (Command) message;
                String name = command.getName();
                logger.info("server command: {}", name);
                try {
                    if (name.equals("_result")) {
                        String resultFor = transactionToCommandMap.get(command.getTransactionId());
                        logger.info("result for method call: {}", resultFor);
                        if (resultFor.equals("connect")) {
                            writeCommandExpectingResult(channel, Command.createStream());
                        } else if (resultFor.equals("createStream")) {
                            streamId = ((Double) command.getArg(0)).intValue();
                            logger.debug("streamId to use: {}", streamId);
                            if (options.getPublishType() != null) {
                                publisher = new ScreenPublisher(screenShare, streamId, options.getBuffer(), false, false) {

                                    @Override
                                    protected RtmpMessage[] getStopMessages(long timePosition) {
                                        return new RtmpMessage[] { Command.unpublish(streamId) };
                                    }
                                };
                                channel.write(Command.publish(streamId, options));
                                return;
                            } else {
                                writer = options.getWriterToSave();
                                if (writer == null) {
                                    writer = new FlvWriter(options.getStart(), options.getSaveAs());
                                }
                                channel.write(Command.play(streamId, options));
                                channel.write(Control.setBuffer(streamId, 0));
                            }
                        } else {
                            logger.warn("un-handled server result for: {}", resultFor);
                        }
                    } else if (name.equals("mousePress")) {
                        double button = ((Double) command.getArg(0));
                        screenShare.mousePress(button);
                        logger.info(name + ": " + button);
                    } else if (name.equals("mouseRelease")) {
                        double button = ((Double) command.getArg(0));
                        screenShare.mouseRelease(button);
                        logger.info(name + ": " + button);
                    } else if (name.equals("mouseMove")) {
                        double x = ((Double) command.getArg(0));
                        double y = ((Double) command.getArg(1));
                        double width = ((Double) command.getArg(2));
                        double height = ((Double) command.getArg(3));
                        screenShare.mouseMove(x, y, width, height);
                        logger.info(name + ": " + x + " " + y + " " + width + " " + height);
                    } else if (name.equals("doubleClick")) {
                        double x = ((Double) command.getArg(0));
                        double y = ((Double) command.getArg(1));
                        double width = ((Double) command.getArg(2));
                        double height = ((Double) command.getArg(3));
                        screenShare.doubleClick(x, y, width, height);
                        logger.info(name + ": " + x + " " + y + " " + width + " " + height);
                    } else if (name.equals("keyPress")) {
                        double key = ((Double) command.getArg(0));
                        screenShare.keyPress(key);
                        logger.info(name + ": " + key);
                    } else if (name.equals("keyRelease")) {
                        double key = ((Double) command.getArg(0));
                        screenShare.keyRelease(key);
                        logger.info(name + ": " + key);
                    } else if (name.equals("onStatus")) {
                        final Map<String, Object> temp = (Map) command.getArg(0);
                        final String code = (String) temp.get("code");
                        logger.info("onStatus code: {}", code);
                        if (code.equals("NetStream.Failed") || code.equals("NetStream.Play.Failed") || code.equals("NetStream.Play.Stop") || code.equals("NetStream.Play.StreamNotFound")) {
                            logger.info("disconnecting, code: {}, bytes read: {}", code, bytesRead);
                            channel.close();
                            return;
                        }
                        if (code.equals("NetStream.Publish.Start") && publisher != null && !publisher.isStarted()) {
                            publisher.start(channel, options.getStart(), options.getLength(), new ChunkSize(4096));
                            return;
                        }
                        if (publisher != null && code.equals("NetStream.Unpublish.Success")) {
                            logger.info("unpublish success, closing channel");
                            ChannelFuture future = channel.write(Command.closeStream(streamId));
                            future.addListener(ChannelFutureListener.CLOSE);
                            return;
                        }
                    } else if (name.equals("close")) {
                        logger.info("server called close, closing channel");
                        channel.close();
                        return;
                    } else if (name.equals("_error")) {
                        logger.error("closing channel, server resonded with error: {}", command);
                        channel.close();
                        return;
                    } else {
                        logger.warn("ignoring server command: {}", command);
                    }
                } catch (Exception e) {
                    logger.error("AMF processing error: {}", e);
                }
                break;
            case BYTES_READ:
                logger.info("ack from server: {}", message);
                break;
            case WINDOW_ACK_SIZE:
                WindowAckSize was = (WindowAckSize) message;
                if (was.getValue() != bytesReadWindow) {
                    channel.write(SetPeerBw.dynamic(bytesReadWindow));
                }
                break;
            case SET_PEER_BW:
                SetPeerBw spb = (SetPeerBw) message;
                if (spb.getValue() != bytesWrittenWindow) {
                    channel.write(new WindowAckSize(bytesWrittenWindow));
                }
                break;
            default:
                logger.info("ignoring rtmp message: {}", message);
        }
        if (publisher != null && publisher.isStarted()) {
            publisher.fireNext(channel, 0);
        }
    }
