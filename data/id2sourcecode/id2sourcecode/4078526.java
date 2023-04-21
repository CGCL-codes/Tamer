    public void write() {
        try {
            workbook.write();
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close/write Excel spreadsheet.");
        } catch (WriteException e) {
            throw new RuntimeException("Unable to write Excel spreadsheet.");
        }
    }
