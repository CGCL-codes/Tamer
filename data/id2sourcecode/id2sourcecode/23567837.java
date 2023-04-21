    private void encodeData(SpreadSheet spreadSheet, SpreadSheetData data, ResponseWriter writer, FacesContext context) throws IOException {
        SpreadSheetModel model = spreadSheet.getSpreadSheetModel();
        int rowCount = model.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            spreadSheet.setRowIndex(row);
            boolean empty = model.isRowEmpty();
            int colCount = data.getColCount();
            for (int col = 0; col < colCount; col++) {
                SpreadSheetColumn column = data.getCols().get(col);
                if (empty) {
                    column.setValue("");
                }
                column.encodeAll(context);
            }
        }
        spreadSheet.setRowIndex(-1);
    }
