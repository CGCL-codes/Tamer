    public void pushMessage(IPipe pipe, IMessage message) {
        if (message instanceof ResetMessage) {
            streamTracker.reset();
        } else if (message instanceof StatusMessage) {
            StatusMessage statusMsg = (StatusMessage) message;
            data.sendStatus(statusMsg.getBody());
        } else if (message instanceof RTMPMessage) {
            RTMPMessage rtmpMsg = (RTMPMessage) message;
            IRTMPEvent msg = rtmpMsg.getBody();
            Header header = new Header();
            int timestamp = streamTracker.add(msg);
            if (timestamp < 0) {
                log.warn("Skipping message with negative timestamp.");
                return;
            }
            header.setTimerRelative(streamTracker.isRelative());
            header.setTimer(timestamp);
            switch(msg.getDataType()) {
                case Constants.TYPE_STREAM_METADATA:
                    Notify notify = new Notify(((Notify) msg).getData().asReadOnlyBuffer());
                    notify.setHeader(header);
                    notify.setTimestamp(header.getTimer());
                    data.write(notify);
                    break;
                case Constants.TYPE_VIDEO_DATA:
                    VideoData videoData = new VideoData(((VideoData) msg).getData().asReadOnlyBuffer());
                    videoData.setHeader(header);
                    videoData.setTimestamp(header.getTimer());
                    video.write(videoData);
                    break;
                case Constants.TYPE_AUDIO_DATA:
                    AudioData audioData = new AudioData(((AudioData) msg).getData().asReadOnlyBuffer());
                    audioData.setHeader(header);
                    audioData.setTimestamp(header.getTimer());
                    audio.write(audioData);
                    break;
                case Constants.TYPE_PING:
                    Ping ping = new Ping(((Ping) msg).getValue1(), ((Ping) msg).getValue2(), ((Ping) msg).getValue3(), ((Ping) msg).getValue4());
                    header.setTimerRelative(false);
                    header.setTimer(0);
                    ping.setHeader(header);
                    ping.setTimestamp(header.getTimer());
                    conn.ping(ping);
                    break;
                case Constants.TYPE_BYTES_READ:
                    BytesRead bytesRead = new BytesRead(((BytesRead) msg).getBytesRead());
                    header.setTimerRelative(false);
                    header.setTimer(0);
                    bytesRead.setHeader(header);
                    bytesRead.setTimestamp(header.getTimer());
                    conn.getChannel((byte) 2).write(bytesRead);
                    break;
                default:
                    data.write(msg);
                    break;
            }
        }
    }
