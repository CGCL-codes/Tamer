        public void monitorQueueSize(Buffer buffer, RTPRawReceiver rtprawreceiver) {
            sizePerPkt = (sizePerPkt + buffer.getLength()) / 2;
            if (format instanceof VideoFormat) {
                if (lastPktSeq + 1L == buffer.getSequenceNumber()) pktsEst++; else pktsEst = 1;
                lastPktSeq = buffer.getSequenceNumber();
                if (RTPSourceStream.mpegVideo.matches(format)) {
                    byte abyte0[] = (byte[]) buffer.getData();
                    int k = buffer.getOffset();
                    int k1 = abyte0[k + 2] & 7;
                    if (k1 < 3 && (buffer.getFlags() & 0x800) != 0) {
                        pktsPerFrame = (pktsPerFrame + pktsEst) / 2;
                        pktsEst = 0;
                    }
                    fps = 30;
                } else if (RTPSourceStream.h264Video.matches(format)) {
                    pktsPerFrame = 300;
                    fps = 15;
                }
                if ((buffer.getFlags() & 0x800) != 0) {
                    pktsPerFrame = (pktsPerFrame + pktsEst) / 2;
                    pktsEst = 0;
                    framesEst++;
                    long l = System.currentTimeMillis();
                    if (l - lastCheckTime >= 1000L) {
                        lastCheckTime = l;
                        fps = (fps + framesEst) / 2;
                        framesEst = 0;
                        if (fps > 30) fps = 30;
                    }
                }
                int i;
                if (bc != null) {
                    i = (int) ((bc.getBufferLength() * fps) / 1000L);
                    if (i <= 0) i = 1;
                    i = pktsPerFrame * i;
                    threshold = (int) (((bc.getMinimumThreshold() * fps) / 1000L) * pktsPerFrame);
                    if (threshold <= i / 2) ;
                    threshold = i / 2;
                } else {
                    i = DEFAULT_PKTS_TO_BUFFER;
                }
                if (RTPSourceStream.h264Video.matches(format)) {
                    maxPktsToBuffer = 200;
                } else {
                    if (maxPktsToBuffer > 0) maxPktsToBuffer = (maxPktsToBuffer + i) / 2; else maxPktsToBuffer = i;
                }
                int i1 = totalPkts();
                if (size > MIN_BUF_CHECK && i1 < size / 4) {
                    if (!prebuffering && tooMuchBufferingCount++ > pktsPerFrame * fps * BUF_CHECK_INTERVAL) {
                        cutByHalf();
                        tooMuchBufferingCount = 0;
                    }
                } else if (i1 >= size / 2 && size < maxPktsToBuffer) {
                    i = size + size / 2;
                    if (i > maxPktsToBuffer) i = maxPktsToBuffer;
                    grow(i + FUDGE);
                    Log.comment("RTP video buffer size: " + size + " pkts, " + i * sizePerPkt + " bytes.\n");
                    tooMuchBufferingCount = 0;
                } else {
                    tooMuchBufferingCount = 0;
                }
                int l1 = (i * sizePerPkt) / 2;
                if (rtprawreceiver != null && l1 > sockBufSize) {
                    rtprawreceiver.setRecvBufSize(l1);
                    if (rtprawreceiver.getRecvBufSize() < l1) sockBufSize = 0x7fffffff; else sockBufSize = l1;
                    Log.comment("RTP video socket buffer size: " + rtprawreceiver.getRecvBufSize() + " bytes.\n");
                }
            } else if (format instanceof AudioFormat) {
                if (sizePerPkt <= 0) sizePerPkt = DEFAULT_AUD_PKT_SIZE;
                if (bc != null) {
                    int j;
                    if (RTPSourceStream.mpegAudio.matches(format)) j = sizePerPkt / 4; else j = DEFAULT_MILLISECS_PER_PKT;
                    int j1 = (int) (bc.getBufferLength() / j);
                    threshold = (int) (bc.getMinimumThreshold() / j);
                    if (threshold <= j1 / 2) ;
                    threshold = j1 / 2;
                    if (j1 > size) {
                        grow(j1);
                        Log.comment("RTP audio buffer size: " + size + " pkts, " + j1 * sizePerPkt + " bytes.\n");
                    }
                    int i2 = (j1 * sizePerPkt) / 2;
                    if (rtprawreceiver != null && i2 > sockBufSize) {
                        rtprawreceiver.setRecvBufSize(i2);
                        if (rtprawreceiver.getRecvBufSize() < i2) sockBufSize = 0x7fffffff; else sockBufSize = i2;
                        Log.comment("RTP audio socket buffer size: " + rtprawreceiver.getRecvBufSize() + " bytes.\n");
                    }
                }
            }
        }
