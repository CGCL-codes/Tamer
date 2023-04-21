    public void Query(String ip) throws Exception {
        this.IPN = IPToInt(ip);
        fis = new RandomAccessFile(this.DBPATH, "r");
        buff = new byte[4];
        fis.seek(0);
        fis.read(buff);
        firstStartIP = this.B2L(buff);
        fis.read(buff);
        lastStartIP = this.B2L(buff);
        recordcount = (int) ((lastStartIP - firstStartIP) / 7);
        if (recordcount <= 1) {
            localStr = country = "Unknown";
            return;
        }
        rangB = 0;
        rangE = recordcount;
        long RecNo;
        do {
            RecNo = (rangB + rangE) / 2;
            GetStartIP(RecNo);
            if (IPN == startIP) {
                rangB = RecNo;
                break;
            }
            if (IPN > startIP) rangB = RecNo; else rangE = RecNo;
        } while (rangB < rangE - 1);
        GetStartIP(rangB);
        GetEndIP();
        GetCountry(IPN);
        try {
            fis.close();
        } catch (Exception e) {
        }
    }
