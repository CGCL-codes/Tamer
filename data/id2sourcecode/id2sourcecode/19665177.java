    public void writeOutSpreadsheet() throws IOException {
        final FileOutputStream fileOut = new FileOutputStream(stocks.get(0).getSymbol() + ".xls");
        workbook.write(fileOut);
        fileOut.close();
    }
