    public static AudioInputStream recvSampleAsync(final SMDIRecvInstance ri) throws SmdiOutOfRangeException, SmdiNoMemoryException, SMDILogicException, SmdiSampleEmptyException, SmdiGeneralException {
        final byte[] reply = SMDIMsg.stripHeader(IN_reqSampleHeader(ri.getHAID(), ri.getID(), ri.getSample()).getReply());
        final AudioFormat af = SMDIMsg.SampleHeader.getAudioFormat(reply, ri);
        final int len = SMDIMsg.SampleHeader.getSampleLength(reply);
        final PipedInputStream pis = new PipedInputStream();
        final PipedOutputStream pos;
        try {
            pos = new PipedOutputStream(pis);
        } catch (IOException e) {
            throw new SmdiGeneralException();
        }
        AudioInputStream ais = new AudioInputStream(pis, af, len);
        new Impl_ZThread() {

            public void runBody() {
                ProgressCallback prog = ri.getProgressCallback();
                try {
                    SMDITransactionReport rep;
                    int ps = ri.getPacketSizeInBytes();
                    prog.updateProgress(0);
                    try {
                        int bytesOutstanding = len * (af.getSampleSizeInBits() / 8) * af.getChannels();
                        rep = IN_beginTransfer(ri.getHAID(), ri.getID(), ri.getSample(), ps);
                        double tot = bytesOutstanding;
                        double gc_pass_tot = 0;
                        int packet = 0;
                        while (bytesOutstanding > 0) {
                            try {
                                rep = IN_sendNextPacket(ri.getHAID(), ri.getID(), ri.getSample(), packet++, ps);
                                byte[] packetData = SMDIMsg.DataPacket.getData(rep.getReply());
                                pos.write(packetData, 0, packetData.length);
                                bytesOutstanding -= packetData.length;
                                gc_pass_tot += packetData.length;
                                if (prog.isCancelled()) {
                                    tryAbort(ri.getHAID(), ri.getID());
                                    throw new SmdiTransferAbortedException();
                                }
                                prog.updateProgress((tot - bytesOutstanding) / tot);
                            } finally {
                                if (gc_pass_tot > GC_BYTE_THRESHOLD) {
                                    System.gc();
                                    gc_pass_tot = 0;
                                }
                            }
                        }
                        pos.close();
                    } catch (SmdiOutOfRangeException e) {
                        e.printStackTrace();
                    } catch (SmdiTransferAbortedException e) {
                        e.printStackTrace();
                    } catch (SmdiNoMemoryException e) {
                        e.printStackTrace();
                    } catch (SMDILogicException e) {
                        e.printStackTrace();
                    } catch (SmdiSampleEmptyException e) {
                        e.printStackTrace();
                    } finally {
                        prog.updateProgress(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return ais;
    }
