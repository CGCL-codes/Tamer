    private void buildProcessUnit(List<Packet> packets, boolean flush) {
        int idx = packets.get(0).getStreamIndex();
        ICodec.Type codec_type = _stream_map.get(idx).decoder.getStreamCoder().getCodecType();
        ProcessUnit pu;
        if (codec_type == ICodec.Type.CODEC_TYPE_VIDEO) {
            pu = new VideoProcessUnit();
        } else if (codec_type == ICodec.Type.CODEC_TYPE_AUDIO) {
            pu = new AudioProcessUnit();
        } else {
            return;
        }
        pu.setInputPackets(packets);
        pu.setDecoder(_stream_map.get(idx).decoder);
        pu.setEncoder(_stream_map.get(idx).encoder);
        pu.setSourceStream(_stream_map.get(idx).instream);
        pu.setTargetStream(_stream_map.get(idx).outstream);
        if (codec_type == ICodec.Type.CODEC_TYPE_VIDEO) {
            pu.setRateCompensateBase(_stream_map.get(idx).frameRateCompensateBase);
            IRational inrate = _stream_map.get(idx).decoder.getFrameRate();
            IRational outrate = _stream_map.get(idx).encoder.getFrameRate();
            int packet_count = packets.size() - (_stream_map.get(idx).decoder.getStreamCoder().getCodecID() == ICodec.ID.CODEC_ID_MPEG2VIDEO ? 1 : 0);
            double in = ((double) packet_count * inrate.getDenominator() / inrate.getNumerator()) * ((double) outrate.getNumerator() / (double) outrate.getDenominator());
            in += _stream_map.get(idx).frameRateCompensateBase;
            _stream_map.get(idx).frameRateCompensateBase = in - Math.floor(in);
            pu.setEsteminatedPacketCount((int) Math.floor(in));
            _log.debug("new VideoProcessUnit queued with size:" + pu.getPacketCount());
        } else if (codec_type == ICodec.Type.CODEC_TYPE_AUDIO) {
            pu.setRateCompensateBase(_stream_map.get(idx).frameRateCompensateBase);
            int channels = _stream_map.get(idx).decoder.getStreamCoder().getChannels();
            channels = channels > 2 ? 2 : channels;
            double inframe_size = (double) (_stream_map.get(idx).in_frame_size * 2 * channels) / (double) (_stream_map.get(idx).decoder.getStreamCoder().getSampleRate()) * (double) (_stream_map.get(idx).encoder.getStreamCoder().getSampleRate());
            long outframe_size = _stream_map.get(idx).out_frame_size * 2 * _stream_map.get(idx).encoder.getStreamCoder().getChannels();
            long size = packets.size() * (long) inframe_size;
            size += _stream_map.get(idx).frameRateCompensateBase;
            long rest = size % outframe_size;
            int estimated_packet_count = Long.valueOf(size / outframe_size).intValue();
            pu.setEsteminatedPacketCount(estimated_packet_count);
            _stream_map.get(idx).frameRateCompensateBase = rest;
            _log.debug("new AudioProcessUnit queued with size:" + pu.getPacketCount());
        }
        try {
            _puqueue.put(pu);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
