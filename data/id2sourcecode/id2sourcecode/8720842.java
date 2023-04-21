    public static int getQuickFileTypeOld(File f) throws Exception {
        InputStream fis = new FileInputStream(f);
        ByteArrayOutputStream is = new ByteArrayOutputStream();
        int read = 0;
        byte[] buf = new byte[512];
        while ((read = fis.read(buf, 0, 512)) > 0) is.write(buf);
        fis.close();
        is.flush();
        is.close();
        byte[] alldata = is.toByteArray();
        byte[] header = new byte[BBD_BLOCK_SIZE];
        byte[] properties;
        byte[] BBD;
        for (int i = 0; i < BBD_BLOCK_SIZE; i++) header[i] = alldata[i];
        for (int i = 0; i < oleSign.length; i++) if (header[i] != (byte) oleSign[i]) {
            System.err.println("Its not OLE.");
            return oleUnknown;
        }
        long sectorSize = 1 << Utils.getShort(header, 0x1e);
        long bbdNumBlocks = Utils.getUInt(header, 0x2c);
        long mblock = Utils.getLong(header, 0x44);
        long msat_size = Utils.getUInt(header, 0x48);
        BBD = new byte[(int) (bbdNumBlocks * sectorSize)];
        byte[] tmpBuf = new byte[MSAT_ORIG_SIZE];
        for (int e = 0, i = 0x4c; e < MSAT_ORIG_SIZE; e++, i++) tmpBuf[e] = header[i];
        int i = 0;
        while ((mblock >= 0) && (i < msat_size)) {
            byte[] newbuf = new byte[(int) sectorSize * (i + 1) + MSAT_ORIG_SIZE];
            for (int j = 0; j < tmpBuf.length; j++) newbuf[j] = tmpBuf[j];
            tmpBuf = newbuf;
            int e = (int) (512 + mblock * sectorSize);
            int finish = e + (int) sectorSize;
            for (int j = MSAT_ORIG_SIZE + (int) sectorSize * i - (i == 0 ? 0 : (4 * i)); e < finish; e++, j++) tmpBuf[j] = alldata[e];
            i++;
            mblock = Utils.getLong(tmpBuf, MSAT_ORIG_SIZE + (int) sectorSize * i - 4);
        }
        for (i = 0; i < bbdNumBlocks; i++) {
            long bbdSector = Utils.getUInt(tmpBuf, 4 * i);
            if (bbdSector >= alldata.length / sectorSize || bbdSector < 0) {
                System.err.println("Bad BBD entry!");
                return oleUnknown;
            }
            int e = (int) (512 + bbdSector * sectorSize);
            int j = i * (int) sectorSize;
            int finish = e + (int) sectorSize;
            for (; e < finish; e++, j++) BBD[j] = alldata[e];
        }
        long propLen = 0;
        long propMaxLen = 5;
        long propNumber = 0;
        long propCurrent = Utils.getLong(header, 0x30);
        long propStart = propCurrent;
        if (propStart >= 0) {
            properties = new byte[(int) (propMaxLen * sectorSize)];
            while (true) {
                int e = (int) (512 + propCurrent * sectorSize);
                int j = (int) (propLen * sectorSize);
                int finish = e + (int) sectorSize;
                for (; e < finish; e++, j++) properties[j] = alldata[e];
                propLen++;
                if (propLen >= propMaxLen) {
                    byte[] newProp = new byte[(int) (propMaxLen * sectorSize)];
                    propMaxLen += 5;
                    for (int ek = 0; ek < properties.length; ek++) newProp[ek] = properties[ek];
                    properties = newProp;
                }
                propCurrent = Utils.getLong(BBD, (int) propCurrent * 4);
                if (propCurrent < 0 || (int) propCurrent >= alldata.length / sectorSize) break;
            }
            propNumber = (propLen * sectorSize) / PROP_BLOCK_SIZE;
        } else {
            System.err.println("No properties.");
            return oleUnknown;
        }
        long propCurNumber = 0;
        while (propCurNumber < propNumber) {
            OleEntry e;
            int nLen;
            byte[] oleBuf = new byte[PROP_BLOCK_SIZE];
            int ie = (int) propCurNumber * PROP_BLOCK_SIZE;
            int finish = ie + PROP_BLOCK_SIZE;
            for (int ee = 0; ie < finish; ee++, ie++) oleBuf[ee] = properties[ie];
            propCurNumber++;
            if (!rightOleType(oleBuf)) {
                System.err.println("Wrong OLE type, break...");
                break;
            }
            e = new OleEntry();
            nLen = Utils.getShort(oleBuf, 0x40);
            for (i = 0; i < (nLen / 2) - 1; i++) e.name += (char) oleBuf[i * 2];
            if (e.name.equalsIgnoreCase("worddocument")) return oleWord; else if (e.name.equalsIgnoreCase("powerpoint document")) return olePpt; else if (e.name.equalsIgnoreCase("workbook") || e.name.equalsIgnoreCase("book")) return oleXls;
        }
        return oleUnknown;
    }
