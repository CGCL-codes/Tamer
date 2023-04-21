    public void load(AudioTrackDO source) {
        ID = source.getID();
        format = source.getFormat();
        format_Info = source.getFormat_Info();
        format_profile = source.getFormat_profile();
        format_version = source.getFormat_version();
        codec_ID = source.getCodec_ID();
        num_channels = (byte) TextUtil.quantify(source.getChannel_s(), "channels");
        sampling_rate = (int) (TextUtil.quantify(source.getSampling_rate(), "KHz") * 1000);
        bit_rate = (int) TextUtil.quantify(source.getBit_rate(), "Kbps");
        title = source.getTitle();
        language = source.getLanguage();
    }
