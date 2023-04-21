    private void ProcessSystem(slinkeRawMsg m) {
        Byte[] msg = m.getMsgData();
        switch(msg[0].intValue()) {
            case 4:
                samplePeriod = ((msg[2].intValue() & 0xff) + ((msg[1].intValue() & 0xff) * 256)) / 5;
                sampleRateRead = true;
                break;
            case 6:
                carrierFreq = 1000 / (((1 << (msg[1].intValue() & 0xff)) * ((msg[2].intValue() & 0xff) + 1)) / 5);
                carrierFreqRead = true;
                break;
            case 11:
                version = msg[1];
                versionRead = true;
                ;
                break;
            case 12:
                int i;
                serialNumber = new Byte[8];
                for (i = 0; i < 8; i++) {
                    serialNumber[i] = msg[i + 1];
                }
                serialRead = true;
                break;
            default:
                break;
        }
    }
