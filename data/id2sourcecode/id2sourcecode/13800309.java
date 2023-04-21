    public LinkedList<IRTMPEvent> getParts() {
        LinkedList<IRTMPEvent> parts = new LinkedList<IRTMPEvent>();
        int position = data.position();
        do {
            byte subType = data.get();
            int size = IOUtils.readUnsignedMediumInt(data);
            log.debug("Data subtype: {} size: {}", subType, size);
            int timestamp = data.getInt();
            timestamp = (timestamp >> 8) | ((timestamp & 0x000000ff) << 24);
            int streamId = IOUtils.readUnsignedMediumInt(data);
            log.debug("Data timestamp: {} stream id: {}", timestamp, streamId);
            Header partHeader = new Header();
            partHeader.setChannelId(header.getChannelId());
            partHeader.setDataType(subType);
            partHeader.setSize(size);
            partHeader.setStreamId(header.getStreamId());
            partHeader.setTimer(timestamp);
            int backPointer = 0;
            switch(subType) {
                case TYPE_AUDIO_DATA:
                    AudioData audio = new AudioData(data.getSlice(size));
                    audio.setTimestamp(timestamp);
                    audio.setHeader(partHeader);
                    log.debug("Audio header: {}", audio.getHeader());
                    parts.add(audio);
                    backPointer = data.getInt();
                    if (backPointer != (size + 11)) {
                        log.debug("Data size ({}) and back pointer ({}) did not match", size, backPointer);
                    }
                    break;
                case TYPE_VIDEO_DATA:
                    VideoData video = new VideoData(data.getSlice(size));
                    video.setTimestamp(timestamp);
                    video.setHeader(partHeader);
                    log.debug("Video header: {}", video.getHeader());
                    parts.add(video);
                    backPointer = data.getInt();
                    if (backPointer != (size + 11)) {
                        log.debug("Data size ({}) and back pointer ({}) did not match", size, backPointer);
                    }
                    break;
                default:
                    log.debug("Non-A/V subtype: {}", subType);
                    Unknown unk = new Unknown(subType, data.getSlice(size));
                    unk.setTimestamp(timestamp);
                    unk.setHeader(partHeader);
                    parts.add(unk);
                    backPointer = data.getInt();
            }
            position = data.position();
        } while (position < data.limit());
        return parts;
    }
