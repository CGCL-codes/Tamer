    public static EncodingInfo createEncodingInfo(AudioFile f) {
        EncodingInfo info = new EncodingInfo();
        info.setBitrate(f.getBitrate());
        info.setChannelNumber(f.getChannelNumber());
        info.setEncodingType(f.getEncodingType());
        info.setExtraEncodingInfos(f.getExtraEncodingInfos());
        info.setLength(f.getLength());
        info.setPreciseLength(f.getPreciseLength());
        info.setSamplingRate(f.getSamplingRate());
        info.setVbr(f.isVbr());
        return info;
    }
