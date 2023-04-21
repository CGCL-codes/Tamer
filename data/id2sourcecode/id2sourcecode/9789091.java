    public void init() throws IOException {
        String fileName = Controler.getIterationFilename(this.spreadsheetFileName);
        File spreadsheetFile = new File(fileName);
        if (spreadsheetFile.exists()) {
            boolean ret = IOUtils.renameFile(fileName, fileName + ".old");
            if (!ret) {
                spreadsheetFile.delete();
            }
        }
        spreadsheetFile.createNewFile();
        BufferedWriter spreadFileWriter = IOUtils.getBufferedWriter(fileName);
        this.spreadSheetWriter = new VDSSignSpreadSheetWriter(spreadFileWriter);
        this.spreadSheetWriter.writeHeader();
    }
