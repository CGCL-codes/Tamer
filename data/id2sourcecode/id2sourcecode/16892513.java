    private void copyFTPToFTP(String sSource, String sTarget) throws FTPException, IOException {
        String sSourceHost, sTargetHost, sSourcePath, sTargetPath, sSourceFile, sTargetFile, sTempName;
        FTPWorkerThread oReader, oWriter;
        FTPClient oFTPC;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin FileSystem.copyFTPToFTP(" + sSource + "," + sTarget + ")");
            DebugFile.incIdent();
        }
        splitURI(sSource);
        sSourceHost = sHost;
        sSourcePath = sPath;
        if (!sSourcePath.endsWith("/")) sSourcePath += "/";
        sSourceFile = sFile;
        splitURI(sTarget);
        sTargetHost = sHost;
        sTargetPath = sPath;
        if (!sTargetPath.endsWith("/")) sTargetPath += "/";
        sTargetFile = sFile;
        if (sSourceHost.equals(sTargetHost) && (OS != OS_PUREJAVA)) {
            sTempName = Gadgets.generateUUID();
            oFTPC = new FTPClient(sTargetHost);
            oFTPC.login(user(), password());
            if (DebugFile.trace) DebugFile.writeln("FTPClient.site(exec cp " + sSourcePath + sSourceFile + " " + sTargetPath + sTargetFile);
            oFTPC.rename(sSourcePath + sSourceFile, sSourcePath + sTempName);
            oFTPC.site("exec cp " + sSourcePath + sTempName + " " + sTargetPath + sTargetFile);
            oFTPC.rename(sSourcePath + sTempName, sSourcePath + sSourceFile);
            oFTPC.quit();
        } else {
            oReader = new FTPWorkerThread(sSourceHost, sUsr, sPwd);
            oWriter = new FTPWorkerThread(sTargetHost, sUsr, sPwd);
            oReader.get(sSourcePath + sSourceFile);
            oWriter.put(sTargetPath + sTargetFile);
            oReader.connect(oWriter.getInputPipe());
            if (DebugFile.trace) DebugFile.writeln("starting read pipe...");
            oReader.start();
            if (DebugFile.trace) DebugFile.writeln("starting write pipe...");
            oWriter.start();
        }
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End FileSystem.copyFTPToFTP()");
        }
    }
