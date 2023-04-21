    public void carve(Runestone stone) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        try {
            final StructureAndContent<String> urls = stone.getStructureAndContent("csv_document_url");
            final Structure documentHasURL = stone.getStructure("csv_document_has_url");
            final Structure cells = stone.getStructure("cell");
            final StructureAndContent<String> texts = stone.getStructureAndContent("cell_text");
            final Structure cellHasText = stone.getStructure("cell_has_text");
            final Structure cellAbove = stone.getStructure("cell_above");
            final Structure cellBelow = stone.getStructure("cell_below");
            final Structure cellLeft = stone.getStructure("cell_left");
            final Structure cellRight = stone.getStructure("cell_right");
            final Structure cellInRow = stone.getStructure("cell_in_row");
            final Structure cellInColumn = stone.getStructure("cell_in_column");
            final Structure rows = stone.getStructure("row");
            final Structure rowHasFirstCell = stone.getStructure("row_has_first_cell");
            final Structure rowPrevious = stone.getStructure("previous_row");
            final Structure rowNext = stone.getStructure("next_row");
            final Structure columns = stone.getStructure("column");
            final Structure colHasFirstCell = stone.getStructure("column_has_first_cell");
            final Structure colPrevious = stone.getStructure("previous_column");
            final Structure colNext = stone.getStructure("next_column");
            final Structure docHasFirstCell = stone.getStructure("document_has_first_cell");
            final Structure docHasFirstRow = stone.getStructure("document_has_first_row");
            final Structure docHasFirstCol = stone.getStructure("document_has_first_column");
            for (final Map.Entry<int[], String> url : urls) {
                final int docID = url.getKey()[0];
                documentHasURL.inscribe(docID, docID);
                CSVReader reader = new CSVReader(new InputStreamReader((new URL(url.getValue())).openStream()));
                List<Integer> previousRow = null;
                List<Integer> colIDs = new ArrayList<Integer>();
                int previousRowID = -1;
                String[] cols;
                while ((cols = reader.readNext()) != null) {
                    final int rowID = rows.encode();
                    final List<Integer> currentRow = new ArrayList<Integer>();
                    for (int c = 0; c < cols.length; ++c) {
                        final int cellID = cells.encode();
                        cellInRow.inscribe(cellID, rowID);
                        currentRow.add(cellID);
                        final int textID = texts.encode(cols[c].trim());
                        cellHasText.inscribe(cellID, textID);
                        if (previousRow != null && previousRow.size() > c) {
                            cellAbove.inscribe(cellID, previousRow.get(c));
                            cellBelow.inscribe(previousRow.get(c), cellID);
                            cellInColumn.inscribe(cellID, colIDs.get(c));
                        }
                        if (c == 0) {
                            rowHasFirstCell.inscribe(rowID, cellID);
                        } else {
                            cellRight.inscribe(currentRow.get(c - 1), cellID);
                            cellLeft.inscribe(cellID, currentRow.get(c - 1));
                        }
                    }
                    if (previousRowID != -1) {
                        rowPrevious.inscribe(rowID, previousRowID);
                        rowNext.inscribe(previousRowID, rowID);
                    } else {
                        int previousCol = -1;
                        docHasFirstCell.inscribe(docID, currentRow.get(0));
                        docHasFirstRow.inscribe(docID, rowID);
                        for (int i = 0; i < currentRow.size(); ++i) {
                            int colID = columns.encode();
                            colIDs.add(colID);
                            cellInColumn.inscribe(currentRow.get(i), colID);
                            if (i == 0) docHasFirstCol.inscribe(docID, colID);
                            colHasFirstCell.inscribe(colID, currentRow.get(i));
                            if (previousCol != -1) {
                                colPrevious.inscribe(colID, previousCol);
                                colNext.inscribe(previousCol, colID);
                            }
                            previousCol = colID;
                        }
                    }
                    previousRow = currentRow;
                    previousRowID = rowID;
                }
            }
        } catch (final IOException e) {
            throw new RunesExceptionRuneExecution(e, this);
        }
    }
