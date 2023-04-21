    public ByteBuffer encodeHeader(Header header, Header lastHeader) {
        byte headerType = HEADER_NEW;
        if (lastHeader == null || header.getStreamId() != lastHeader.getStreamId() || !header.isTimerRelative()) {
            headerType = HEADER_NEW;
        } else if (header.getSize() != lastHeader.getSize() || header.getDataType() != lastHeader.getDataType()) {
            headerType = HEADER_SAME_SOURCE;
        } else if (header.getTimer() != lastHeader.getTimer()) {
            headerType = HEADER_TIMER_CHANGE;
        } else {
            headerType = HEADER_CONTINUE;
        }
        final ByteBuffer buf = ByteBuffer.allocate(RTMPUtils.getHeaderLength(headerType));
        final byte headerByte = RTMPUtils.encodeHeaderByte(headerType, header.getChannelId());
        buf.put(headerByte);
        switch(headerType) {
            case HEADER_NEW:
                RTMPUtils.writeMediumInt(buf, header.getTimer());
                RTMPUtils.writeMediumInt(buf, header.getSize());
                buf.put(header.getDataType());
                RTMPUtils.writeReverseInt(buf, header.getStreamId());
                break;
            case HEADER_SAME_SOURCE:
                RTMPUtils.writeMediumInt(buf, header.getTimer());
                RTMPUtils.writeMediumInt(buf, header.getSize());
                buf.put(header.getDataType());
                break;
            case HEADER_TIMER_CHANGE:
                RTMPUtils.writeMediumInt(buf, header.getTimer());
                break;
            case HEADER_CONTINUE:
                break;
        }
        return buf;
    }
