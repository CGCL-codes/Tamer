    private void writeAudioDescriptor(StringBuilder builder, RTPFormats formats) {
        builder.append("m=audio %s RTP/AVP ");
        builder.append(payloads(formats));
        builder.append("\n");
        formats.rewind();
        while (formats.hasMore()) {
            RTPFormat f = formats.next();
            String rtpmap = null;
            AudioFormat fmt = (AudioFormat) f.getFormat();
            if (fmt.getChannels() == 1) {
                rtpmap = String.format("a=rtpmap:%d %s/%d\n", f.getID(), fmt.getName(), f.getClockRate());
            } else {
                rtpmap = String.format("a=rtpmap:%d %s/%d/%d\n", f.getID(), fmt.getName(), f.getClockRate(), fmt.getChannels());
            }
            builder.append(rtpmap);
            if (f.getFormat().getOptions() != null) {
                builder.append(String.format("a=fmtp:%d %s\n", f.getID(), f.getFormat().getOptions()));
            }
        }
    }
