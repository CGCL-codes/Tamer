    public String readfilestr(String sFilePath, String sEncoding) throws MalformedURLException, FTPException, FileNotFoundException, IOException, OutOfMemoryError {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin FileSystem.readfilestr(" + sFilePath + "," + sEncoding + ")");
            DebugFile.incIdent();
        }
        String sRetVal;
        String sLower = sFilePath.toLowerCase();
        if (sLower.startsWith("file://")) sFilePath = sFilePath.substring(7);
        if (sLower.startsWith("http://") || sLower.startsWith("https://")) {
            URL oUrl = new URL(sFilePath);
            ByteArrayOutputStream oStrm = new ByteArrayOutputStream();
            DataHandler oHndlr = new DataHandler(oUrl);
            oHndlr.writeTo(oStrm);
            sRetVal = oStrm.toString(sEncoding);
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
                sRetVal = oStrm.toString(sEncoding);
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
            byte byBuffer[] = new byte[3];
            char aBuffer[] = new char[iFLen];
            StringBuffer oBuffer = new StringBuffer(iFLen);
            BufferedInputStream oBfStrm;
            FileInputStream oInStrm;
            InputStreamReader oReader;
            if (sEncoding == null) {
                oInStrm = new FileInputStream(oFile);
                oBfStrm = new BufferedInputStream(oInStrm, iFLen);
                int iReaded = oBfStrm.read(byBuffer, 0, 3);
                if (iReaded > 2) if (byBuffer[0] == -1 && byBuffer[1] == -2) sEncoding = "UTF-16LE"; else if (byBuffer[0] == -2 && byBuffer[1] == -1) sEncoding = "UTF-16BE"; else if (byBuffer[0] == -17 && byBuffer[1] == -69 && byBuffer[2] == -65) sEncoding = "UTF-8"; else sEncoding = "ISO-8859-1"; else sEncoding = "ISO-8859-1";
                if (DebugFile.trace) DebugFile.writeln("encoding is " + sEncoding);
                oBfStrm.close();
                oInStrm.close();
            }
            if (iFLen > 0) {
                oInStrm = new FileInputStream(oFile);
                oBfStrm = new BufferedInputStream(oInStrm, iFLen);
                oReader = new InputStreamReader(oBfStrm, sEncoding);
                int iReaded = oReader.read(aBuffer, 0, iFLen);
                int iSkip = ((int) aBuffer[0] == 65279 || (int) aBuffer[0] == 65534 ? 1 : 0);
                oBuffer.append(aBuffer, iSkip, iReaded - iSkip);
                aBuffer = null;
                oReader.close();
                oBfStrm.close();
                oInStrm.close();
                oReader = null;
                oInStrm = null;
                oFile = null;
                sRetVal = oBuffer.toString();
            } else sRetVal = "";
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End FileSystem.readfilestr() : " + String.valueOf(sRetVal.length()));
        }
        return sRetVal;
    }
