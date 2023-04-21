    public void writeSpreadsheetToFile(File f) {
        try {
            PrintWriter pw = new PrintWriter(f);
            pw.println(createStringFromRow(header));
            for (int i = 0; i < spreadsheet.length; i++) {
                if (spreadsheet[i] != null) {
                    pw.println(createStringFromRow(spreadsheet[i]));
                } else {
                    System.out.println("NULL: " + i);
                }
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            System.err.println("ERROR writing spreadsheet to " + f.getName());
        }
    }
