    public synchronized void putData(DataType dataType, long ts, byte[] buf, int size) {
        if (timeBase == 0) {
            timeBase = ts;
        }
        int currentTime = (int) (ts - timeBase);
        ITag tag = new Tag(dataType.getId(), currentTime, size + 1, null, prevSize);
        prevSize = size + 1;
        byte tagType = 0;
        switch(dataType) {
            case AUDIO:
                tagType = (byte) ((byte) ((encoder.getMetaData().getAudioCodec().getId() << 4)) | (IoConstants.FLAG_SIZE_16_BIT << 1));
                tagType |= encoder.getMetaData().getRateType().getId() << 2;
                tagType |= encoder.getMetaData().getChannelType().getId();
                break;
            case KEY_FRAME:
                tag.setDataType(DataType.VIDEO.getId());
                tagType = (byte) (encoder.getMetaData().getVideoCodec().getId() << 0);
                tagType |= dataType.getId() << 4;
                break;
            case INTER_FRAME:
                tag.setDataType(DataType.VIDEO.getId());
                tagType = (byte) (encoder.getMetaData().getVideoCodec().getId() << 0);
                tagType |= dataType.getId() << 4;
                break;
            case DISPOSABLE_INTER_FRAME:
                break;
        }
        IoBuffer body = IoBuffer.allocate(tag.getBodySize());
        body.setAutoExpand(true);
        body.put(tagType);
        body.put((byte[]) resizeArray(buf, size));
        body.flip();
        body.limit(tag.getBodySize());
        tag.setBody(body);
        dataQueue.add(tag);
    }
