    public int write() {
        File fileTemp = new File(trustedLocationLocal);
        byte[] fileBytes = null;
        int result = 0;
        byte[] asn1Block = generateASN1Block();
        if (asn1Block == null) {
            return -1;
        }
        try {
            if (!fileTemp.exists()) {
                fileTemp.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(fileTemp, "rw");
            raf.setLength(0);
            try {
                Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
                fileBytes = cipher.doFinal(asn1Block);
                raf.write(fileBytes);
            } catch (Exception e) {
                this.showMessage("Using secretKey to write TCB failed", SAWSTextOutputCallback.ERROR);
                if (debugLevel >= SAWSConstant.ErrorInfo) sawsDebugLog.write(e + "\nUsing secretKey to write TCB failed");
                result = -1;
            }
            raf.close();
        } catch (Exception e2) {
            if (debugLevel >= SAWSConstant.ErrorInfo) sawsDebugLog.write(e2);
            result = -1;
        }
        return result;
    }
