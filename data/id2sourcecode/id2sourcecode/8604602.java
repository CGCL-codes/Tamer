    public ProcessUnit buildProcessUnit(List<Packet> packets) {
        int idx = packets.get(0).getStreamIndex();
        StreamData stream = _stream_map.get(idx);
        ICodec.Type codec_type = stream.decoder.getStreamCoder().getCodecType();
        ProcessUnit pu;
        if (codec_type == ICodec.Type.CODEC_TYPE_VIDEO) {
            pu = new VideoProcessUnit();
        } else if (codec_type == ICodec.Type.CODEC_TYPE_AUDIO) {
            pu = new AudioProcessUnit();
        } else {
            return null;
        }
        pu.setInputPackets(packets);
        pu.setDecoder(stream.decoder);
        pu.setEncoder(stream.encoder);
        pu.setSourceStream(stream.instream);
        pu.setTargetStream(stream.outstream);
        if (codec_type == ICodec.Type.CODEC_TYPE_VIDEO) {
            pu.setRateCompensateBase(stream.frameRateCompensateBase);
            IRational inrate = stream.decoder.getFrameRate();
            IRational outrate = stream.encoder.getFrameRate();
            _log.debug(inrate.toString());
            _log.debug(outrate.toString());
            ICodec.ID id = stream.decoder.getStreamCoder().getCodecID();
            int packet_count = packets.size() - (stream.decoder.getStreamCoder().getCodecID().equals(ICodec.ID.CODEC_ID_MPEG2VIDEO) ? 1 : 0);
            BigDecimal dec = new BigDecimal(packet_count);
            dec = dec.multiply(new BigDecimal(inrate.getDenominator()));
            dec = dec.divide(new BigDecimal(inrate.getNumerator()), 100, BigDecimal.ROUND_HALF_EVEN);
            dec = dec.multiply(new BigDecimal(outrate.getNumerator()));
            dec = dec.divide(new BigDecimal(outrate.getDenominator()));
            double in = dec.doubleValue();
            in += stream.frameRateCompensateBase;
            stream.frameRateCompensateBase = in - Math.floor(in);
            pu.setEsteminatedPacketCount((int) Math.floor(in));
            _log.debug("new VideoProcessUnit queued with size:" + pu.getPacketCount());
        } else if (codec_type == ICodec.Type.CODEC_TYPE_AUDIO) {
            pu.setRateCompensateBase(stream.frameRateCompensateBase);
            int dec_frame_size = stream.decoder.getStreamCoder().getAudioFrameSize();
            int enc_frame_size = stream.encoder.getStreamCoder().getAudioFrameSize();
            int dec_channels = stream.decoder.getStreamCoder().getChannels();
            int enc_channels = stream.encoder.getStreamCoder().getChannels();
            int dec_sample_rate = stream.decoder.getStreamCoder().getSampleRate();
            int enc_sample_rate = stream.encoder.getStreamCoder().getSampleRate();
            dec_channels = dec_channels > 2 ? 2 : dec_channels;
            double inframe_size = (double) (dec_frame_size * 2 * dec_channels) / (double) (dec_sample_rate) * (double) (enc_sample_rate);
            long outframe_size = enc_frame_size * 2 * enc_channels;
            long size = (long) ((double) packets.size() * (double) inframe_size);
            size += stream.frameRateCompensateBase;
            long rest = size % outframe_size;
            int estimated_packet_count = Long.valueOf(size / outframe_size).intValue();
            pu.setEsteminatedPacketCount(estimated_packet_count);
            stream.frameRateCompensateBase = rest;
            _log.debug("new AudioProcessUnit queued with size:" + pu.getPacketCount());
        }
        return pu;
    }
