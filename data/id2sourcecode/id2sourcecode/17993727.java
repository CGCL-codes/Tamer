    void processBigSMS() throws Exception {
        String pdu = null;
        int j;
        Modem modem = new Modem("COM4", "115200");
        if (!isBig()) {
            try {
                pdu = getPDU(no, 0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            j = pdu.length();
            j /= 2;
            if (no == null) ; else if (no.length() == 0) j--; else {
                int smscNumberLen = no.length();
                if (no.charAt(0) == '+') smscNumberLen--;
                if (smscNumberLen % 2 != 0) smscNumberLen++;
                int smscLen = (2 + smscNumberLen) / 2;
                j = j - smscLen - 1;
            }
        } else {
            System.out.println("Sending Long Message. In " + getNoOfParts() + " parts.");
            for (int partNo = 1; partNo <= getNoOfParts(); partNo++) {
                System.out.println("__ Sending part " + partNo + " __");
                try {
                    pdu = getPDU(no, mpRefNo, partNo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                j = pdu.length();
                j /= 2;
                if (no == null) ; else if (no.length() == 0) j--; else {
                    int smscNumberLen = no.length();
                    if (no.charAt(0) == '+') smscNumberLen--;
                    if (smscNumberLen % 2 != 0) smscNumberLen++;
                    int smscLen = (2 + smscNumberLen) / 2;
                    j = j - smscLen - 1;
                }
                System.out.println("Message index ::" + j + "\n Message to Send :" + pdu);
                modem.SendSms("+919922930640", pdu, 2);
            }
            outMpRefNo = (outMpRefNo + 1) % 65536;
        }
    }
