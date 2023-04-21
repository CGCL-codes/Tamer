    public byte[] readfilebin(String sFilePath) throws MalformedURLException, FTPException, FileNotFoundException, IOException, OutOfMemoryError {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin FileSystem.readfilebin(" + sFilePath + ")");
            DebugFile.incIdent();
        }
        byte[] aRetVal;
        String sLower = sFilePath.toLowerCase();
        if (sLower.startsWith("file://")) sFilePath = sFilePath.substring(7);
        if (sLower.startsWith("http://") || sLower.startsWith("https://")) {
            URL oUrl = new URL(sFilePath);
            ByteArrayOutputStream oStrm = new ByteArrayOutputStream();
            DataHandler oHndlr = new DataHandler(oUrl);
            oHndlr.writeTo(oStrm);
            aRetVal = oStrm.toByteArray();
            oStrm.close();
        } else if (sLower.startsWith("ftp://")) {
            FTPClient oFTPC = null;
            boolean bFTPSession = false;
            splitURI(sFilePath);
            try {
                if (DebugFile.trace) DebugFile.writeln("new FTPClient(" + sHost + ")");
                oFTPC = new FTPClient(sHost);
                if (DebugFile.trace) DebugFile.writeln("FTPClient.login(" + sUsr + "," + sPwd + ")");
                oFTPC.login(sUsr, sPwd);
                bFTPSession = true;
                if (DebugFile.trace) DebugFile.writeln("FTPClient.chdir(" + sPath + ")");
                oFTPC.chdir(sPath);
                ByteArrayOutputStream oStrm = new ByteArrayOutputStream();
                oFTPC.setType(FTPTransferType.BINARY);
                if (DebugFile.trace) DebugFile.writeln("FTPClient.get(" + sPath + sFile + "," + sFile + ",false)");
                oFTPC.get(oStrm, sFile);
                aRetVal = oStrm.toByteArray();
                oStrm.close();
            } catch (FTPException ftpe) {
                throw new FTPException(ftpe.getMessage());
            } finally {
                if (DebugFile.trace) DebugFile.writeln("FTPClient.quit()");
                if (bFTPSession) oFTPC.quit();
            }
        } else {
            File oFile = new File(sFilePath);
            int iFLen = (int) oFile.length();
            BufferedInputStream oBfStrm;
            FileInputStream oInStrm;
            if (iFLen > 0) {
                aRetVal = new byte[iFLen];
                oInStrm = new FileInputStream(oFile);
                oBfStrm = new BufferedInputStream(oInStrm, iFLen);
                int iReaded = oBfStrm.read(aRetVal, 0, iFLen);
                oBfStrm.close();
                oInStrm.close();
                oInStrm = null;
                oFile = null;
            } else aRetVal = null;
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End FileSystem.readfilebin()");
        }
        return aRetVal;
    }
