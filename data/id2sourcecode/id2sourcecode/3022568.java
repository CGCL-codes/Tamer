    private String convertOutgoingToIncoming(String pdu) {
        if (log.isTraceEnabled()) {
            log.trace("TpduUtilsTest.convertOutgoingToIncoming()");
            log.trace("\tOUTGOING:\t" + pdu);
        }
        try {
            int byteZero;
            int pid;
            int dcs;
            byte[] userData;
            {
                PduInputStream in = new PduInputStream(pdu);
                TpduUtils.decodeMsisdnFromAddressField(in, true);
                byteZero = in.read();
                int messageReference = in.read();
                {
                    int length = in.read();
                    int byte0 = in.read();
                    String s = "";
                    for (int i = 0; i < ((length + 1) >> 1); ++i) s += in.read();
                }
                pid = in.read();
                dcs = in.read();
                in.read();
                {
                    ByteArrayOutputStream ud = new ByteArrayOutputStream();
                    int read;
                    try {
                        while ((read = in.read()) != -1) ud.write(read);
                    } catch (EOFException ex) {
                    }
                    userData = ud.toByteArray();
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            {
                out.write(TpduUtils.encodeMsisdnAsAddressField("+447890123456", true));
                out.write(byteZero);
                boolean hasUdh = (byteZero & TpduUtils.TP_UDHI) != 0;
                out.write(TpduUtils.encodeMsisdnAsAddressField("+447988156555", false));
                out.write(pid);
                out.write(dcs);
                for (int i = 0; i < 7; ++i) out.write(0);
            }
            out.write(userData);
            String incomingPdu = HexUtils.encode(out.toByteArray());
            if (log.isTraceEnabled()) {
                log.trace("\tINCOMING:\t" + incomingPdu);
                log.trace("TpduUtilsTest.convertOutgoingToIncoming()");
            }
            return incomingPdu;
        } catch (IOException ex) {
            return null;
        }
    }
