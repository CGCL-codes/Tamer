    private boolean moveFTPToFTP(String sSourceURI, String sTargetURI) throws FTPException, IOException {
        boolean bRetVal = true;
        String sSourceHost, sTargetHost, sSourcePath, sTargetPath, sSourceFile, sTargetFile;
        FTPWorkerThread oReader, oWriter;
        FTPClient oFTPC;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin FileSystem.moveFTPToFTP(" + sSourceURI + "," + sTargetURI + ")");
            DebugFile.incIdent();
        }
        splitURI(sSourceURI);
        sSourceHost = sHost;
        sSourcePath = sPath;
        if (!sSourcePath.endsWith("/")) sSourcePath += "/";
        sSourceFile = sFile;
        splitURI(sTargetURI);
        sTargetHost = sHost;
        sTargetPath = sPath;
        if (!sTargetPath.endsWith("/")) sTargetPath += "/";
        sTargetFile = sFile;
        if (sSourceHost.equals(sTargetHost)) {
            if (DebugFile.trace) DebugFile.writeln("new FTPClient(" + sTargetHost + ")");
            oFTPC = new FTPClient(sTargetHost);
            oFTPC.login(user(), password());
            if (DebugFile.trace) DebugFile.writeln("FTPClient.rename(" + sSourcePath + sSourceFile + "," + sTargetPath + sTargetFile + ")");
            oFTPC.rename(sSourcePath + sSourceFile, sTargetPath + sTargetFile);
            oFTPC.quit();
        } else {
            oReader = new FTPWorkerThread(sSourceHost, sUsr, sPwd);
            oWriter = new FTPWorkerThread(sTargetHost, sUsr, sPwd);
            oReader.move(sSourcePath + sSourceFile);
            oWriter.put(sTargetPath + sTargetFile);
            oReader.connect(oWriter.getInputPipe());
            if (DebugFile.trace) DebugFile.writeln("starting read pipe...");
            oReader.start();
            if (DebugFile.trace) DebugFile.writeln("starting write pipe...");
            oWriter.start();
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End FileSystem.moveFTPToFTP()");
        }
        return bRetVal;
    }
