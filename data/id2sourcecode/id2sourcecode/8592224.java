    public String downloadDataTableAsExcell(Spreadsheet spreadsheet) throws Exception {
        String filename = (spreadsheet.getName() + (new Date()).toString()).replaceAll(" ", "_").replaceAll(":", "-") + ".xls";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getServletContext().getRealPath("/") + DOWNLOAD_DIR + filename, false));
            out.write("#Name: " + spreadsheet.getName());
            out.newLine();
            out.write("#Organism: " + spreadsheet.getSpecies());
            out.newLine();
            out.write("#Rows: " + spreadsheet.getNumberOfRows());
            out.newLine();
            out.newLine();
            for (int i = 0; i < spreadsheet.getNumberOfColumns(); i++) {
                if (i > 0) out.write("\t");
                out.write(spreadsheet.getColumnLabel(i));
            }
            out.newLine();
            for (int i = 0; i < spreadsheet.getNumberOfRows(); i++) {
                for (int j = 0; j < spreadsheet.getNumberOfColumns(); j++) {
                    if (j > 0) out.write("\t");
                    out.write(spreadsheet.get(i, j));
                }
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            System.out.println("There was a problem:" + e);
            throw e;
        }
        return DOWNLOAD_DIR.replaceAll("\\\\", "/") + filename;
    }
