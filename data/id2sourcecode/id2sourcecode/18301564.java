    public synchronized int available() throws IOException {
        int avail = super.available();
        int unencoded = precount - prepos + in.available();
        if (encoder.getEncoder().getVbr()) {
            switch(mode) {
                case 0:
                    return avail + (27 + 2 * packetsPerOggPage) * (unencoded / (packetsPerOggPage * framesPerPacket * 320));
                case 1:
                    return avail + (27 + 2 * packetsPerOggPage) * (unencoded / (packetsPerOggPage * framesPerPacket * 640));
                case 2:
                    return avail + (27 + 3 * packetsPerOggPage) * (unencoded / (packetsPerOggPage * framesPerPacket * 1280));
                default:
                    return avail;
            }
        } else {
            int spxpacketsize = encoder.getEncoder().getEncodedFrameSize();
            if (channels > 1) {
                spxpacketsize += 17;
            }
            spxpacketsize *= framesPerPacket;
            spxpacketsize = (spxpacketsize + 7) >> 3;
            int oggpacketsize = 27 + packetsPerOggPage * (spxpacketsize + 1);
            int pcmframesize;
            switch(mode) {
                case 0:
                    pcmframesize = framesPerPacket * 320 * encoder.getChannels();
                    avail += oggpacketsize * (unencoded / (packetsPerOggPage * pcmframesize));
                    return avail;
                case 1:
                    pcmframesize = framesPerPacket * 640 * encoder.getChannels();
                    avail += oggpacketsize * (unencoded / (packetsPerOggPage * pcmframesize));
                    return avail;
                case 2:
                    pcmframesize = framesPerPacket * 1280 * encoder.getChannels();
                    avail += oggpacketsize * (unencoded / (packetsPerOggPage * pcmframesize));
                    return avail;
                default:
                    return avail;
            }
        }
    }
