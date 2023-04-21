    public int exportResult(String fileName, String separator, String quoteString, boolean withQuotes, boolean withTitles) {
        int result = 0;
        File theFile = new File(fileName);
        if (theFile.exists()) {
            int reply = JOptionPane.showConfirmDialog(null, fileName + " already exists. Overwrite it?", "File exists", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                if (doExport(fileName, separator, quoteString, withQuotes, withTitles)) {
                    result = tableModel.getRowCount();
                }
            }
        } else {
            if (doExport(fileName, separator, quoteString, withQuotes, withTitles)) {
                result = tableModel.getRowCount();
            }
        }
        return result;
    }
