    public void saveResults() {
        try {
            String resultSpreadSheet = new StringBuffer(spreadSheetFileName).insert(this.spreadSheetFileName.lastIndexOf('.'), ".results").toString();
            OutputStream fout = new FileOutputStream(resultSpreadSheet);
            getWorkBook().write(fout);
            fout.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to write to results spreadsheet.", e);
        }
    }
