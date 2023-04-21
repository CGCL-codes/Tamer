    public void process() {
        int channels = _dec.getStreamCoder().getChannels() > 2 ? 2 : _dec.getStreamCoder().getChannels();
        _dec.getStreamCoder().setProperty("request_channel_layout", channels);
        _dec.getStreamCoder().setProperty("request_channels", channels);
        _dec.getStreamCoder().setChannels(2);
        _dec.open();
        _enc.open();
        long frame_size = _enc.getStreamCoder().getPropertyAsLong("frame_size");
        if (_dec.getStreamCoder().getChannels() > 2) System.exit(1);
        IAudioResampler resampler = IAudioResampler.make(_enc.getStreamCoder().getChannels(), channels, _enc.getStreamCoder().getSampleRate(), _dec.getStreamCoder().getSampleRate(), _enc.getStreamCoder().getSampleFormat(), _dec.getStreamCoder().getSampleFormat());
        IAudioSamples insamples = IAudioSamples.make(_dec.getStreamCoder().getAudioFrameSize(), channels, _dec.getStreamCoder().getSampleFormat());
        IAudioSamples outsamples = IAudioSamples.make(_enc.getStreamCoder().getAudioFrameSize(), _enc.getStreamCoder().getChannels(), _enc.getStreamCoder().getSampleFormat());
        IPacket outpacket = IPacket.make();
        int samples_offset = (int) Math.floor(_rateCompensate);
        if (samples_offset > 0) {
            samples_offset += 64;
            IAudioSamples dummysamples = IAudioSamples.make(IBuffer.make(null, new byte[samples_offset], 0, samples_offset), _enc.getStreamCoder().getChannels(), _enc.getStreamCoder().getSampleFormat());
            dummysamples.setComplete(true, samples_offset, _enc.getStreamCoder().getSampleRate(), _enc.getStreamCoder().getChannels(), _enc.getStreamCoder().getSampleFormat(), 0);
            int error = _enc.getStreamCoder().encodeAudio(outpacket, dummysamples, 0);
            _encoded_bytes += error;
            if (error < 0) {
                _log.error("encoding audio");
            }
        }
        for (Packet p : _input_packets) {
            if ((_decoded_bytes += _dec.getStreamCoder().decodeAudio(insamples, p.getPacket(), 0)) < 0) {
                _log.error("decoding audio");
            }
            if (!insamples.isComplete()) {
                continue;
            }
            _resampled_bytes += resampler.resample(outsamples, insamples, 0);
            for (int consumed = 0; consumed < outsamples.getNumSamples(); ) {
                int result = _enc.getStreamCoder().encodeAudio(outpacket, outsamples, consumed);
                _encoded_bytes += result;
                if (result < 0) {
                    _log.error("encoding audio");
                } else consumed += result;
                if (outpacket.isComplete()) {
                    outpacket.setDuration(frame_size);
                    _output_packets.add(new Packet(outpacket));
                }
            }
        }
        boolean encode = true;
        while (encode) {
            _encoded_bytes += _enc.getStreamCoder().encodeAudio(outpacket, null, 0);
            if (outpacket.isComplete()) {
                _output_packets.add(new Packet(outpacket));
            } else {
                encode = false;
            }
        }
        for (int a = 0; false && a < 3; a++) {
            outsamples.put(new byte[5000], 0, 0, 5000);
            int result = _enc.getStreamCoder().encodeAudio(outpacket, outsamples, 0);
            if (result < 0) {
                _log.error("encoding audio");
            }
            if (outpacket.isComplete()) {
                _log.debug("AudioPacket completed" + outpacket.toString());
                outpacket.setDuration(frame_size);
                _output_packets.add(new Packet(outpacket));
            }
        }
        _dec.close();
        _enc.close();
        _input_packets.clear();
        if (_esteminatedPacketCount > 0 && _esteminatedPacketCount != _output_packets.size()) {
            _log.warn("PacketCount " + _output_packets.size() + " diff from expeceted PacketCount " + _esteminatedPacketCount + " for ProccessUnit#" + getPuId());
        }
        resampler.delete();
        insamples.delete();
        outsamples.delete();
        outpacket.delete();
    }
