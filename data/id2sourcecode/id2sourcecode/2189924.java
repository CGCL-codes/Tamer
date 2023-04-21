        void sendDAC(VPot pot) throws HardwareInterfaceException {
            int chan = pot.getChannel();
            int value = pot.getBitValue();
            byte[] b = new byte[6];
            byte msb = (byte) (0xff & ((0xf00 & value) >> 8));
            byte lsb = (byte) (0xff & value);
            byte dat1 = 0;
            byte dat2 = (byte) 0xC0;
            byte dat3 = 0;
            dat1 |= (0xff & ((chan % 16) & 0xf));
            dat2 |= ((msb & 0xf) << 2) | ((0xff & (lsb & 0xc0) >> 6));
            dat3 |= (0xff & ((lsb << 2)));
            if (chan < 16) {
                b[0] = dat1;
                b[1] = dat2;
                b[2] = dat3;
                b[3] = 0;
                b[4] = 0;
                b[5] = 0;
            } else {
                b[0] = 0;
                b[1] = 0;
                b[2] = 0;
                b[3] = dat1;
                b[4] = dat2;
                b[5] = dat3;
            }
            sendCmd(CMD_VDAC, 0, b);
        }
