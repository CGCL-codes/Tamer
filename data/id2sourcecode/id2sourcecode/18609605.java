    public static MediaFile toMediaFile(IContainer c, MediaFile file) {
        File f = new File(c.getURL());
        file.setFilename(f.getName());
        file.setPath(f.getParent());
        file.setSize(c.getFileSize());
        file.setContainerType(c.getContainerFormat().getInputFormatShortName());
        file.setTitle(c.getMetaData().getValue("title"));
        file.setAuthor(c.getMetaData().getValue("author"));
        file.setCopyright(c.getMetaData().getValue("copyright"));
        file.setComment(c.getMetaData().getValue("comment"));
        file.setAlbum(c.getMetaData().getValue("album"));
        if (c.getMetaData().getValue("year") != null) {
            file.setYear(Integer.parseInt(c.getMetaData().getValue("year")));
        }
        if (c.getMetaData().getValue("track") != null) {
            file.setTrack(Integer.parseInt(c.getMetaData().getValue("track")));
        }
        if (c.getMetaData().getValue("genre") != null) {
            file.setGenre(Integer.parseInt(c.getMetaData().getValue("genre")));
        }
        file.setDuration(c.getDuration());
        file.setBitrate(new Long(c.getBitRate()));
        file.setInsertDate(new java.util.Date());
        file.setParent(null);
        file.setFileType(0);
        int nb_streams = c.getNumStreams();
        for (int a = 0; a < nb_streams; a++) {
            IStream stream = c.getStream(a);
            IStreamCoder codec = stream.getStreamCoder();
            if (codec.getCodecType() != ICodec.Type.CODEC_TYPE_VIDEO && codec.getCodecType() != ICodec.Type.CODEC_TYPE_AUDIO) continue;
            MediaStream s = new MediaStream();
            s.setStreamIndex(stream.getIndex());
            s.setStreamType(codec.getCodecType().swigValue());
            s.setCodecType(codec.getCodecType().swigValue());
            s.setCodecId(codec.getCodecID().swigValue());
            s.setFrameRateNum(stream.getFrameRate().getNumerator());
            s.setFrameRateDen(stream.getFrameRate().getDenominator());
            s.setStartTime(stream.getStartTime());
            s.setFirstDts(stream.getFirstDts());
            s.setDuration(stream.getDuration());
            s.setNumFrames(stream.getNumFrames());
            s.setTimeBaseNum(stream.getTimeBase().getNumerator());
            s.setTimeBaseDen(stream.getTimeBase().getDenominator());
            s.setCodecTimeBaseNum(codec.getTimeBase().getNumerator());
            s.setCodecTimeBaseDen(codec.getTimeBase().getDenominator());
            if (codec.getPropertyAsLong("ticks_per_frame") > 0) {
                s.setTicksPerFrame(new Long(codec.getPropertyAsLong("ticks_per_frame")).intValue());
            }
            s.setWidth(codec.getWidth());
            s.setHeight(codec.getHeight());
            s.setGopSize(codec.getNumPicturesInGroupOfPictures());
            s.setPixelFormat(codec.getPixelType().swigValue());
            s.setBitrate(codec.getBitRate());
            s.setSampleRate(codec.getSampleRate());
            s.setChannels(codec.getChannels());
            s.setSampleFormat(codec.getSampleFormat().swigValue());
            if (codec.getPropertyAsLong("bits_per_coded_sample") > 0) {
                s.setBitsPerCodedSample(new Long(codec.getPropertyAsLong("bits_per_coded_sample")).intValue());
            }
            Collection<String> keys = codec.getPropertyNames();
            for (String key : keys) {
                s.setAttribute(key, codec.getPropertyAsString(key));
            }
            file.addStream(s);
        }
        return file;
    }
