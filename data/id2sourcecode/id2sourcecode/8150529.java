    public void testWriteSpreadsheet2() throws Exception {
        Spreadsheet table = new Spreadsheet(10, 1);
        table.setEditableByDefault(true);
        table.setCellAt("1", 1, 0);
        table.setCellAt("cell(1,0)", 9, 0);
        table.setCellAt("0", 8, 0);
        table.setCellAt("cell(1,0) - cell(9,0)", 5, 0);
        table.setCellAt("cell(5,0)", 4, 0);
        table.setCellAt("cell(4,0) + cell(1,0)", 3, 0);
        table.setCellAt("cell(8,0)", 7, 0);
        table.setCellAt("cell(7,0) * 1", 6, 0);
        table.setCellAt("cell(1,0) + cell(3,0)", 2, 0);
        table.setCellAt("cell(1,0)+cell(2,0)+cell(3,0)+cell(4,0)+cell(5,0)" + "cell(6,0)+cell(7,0)+cell(8,0)+cell(9,0)", 0, 0);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        table.writeSpreadsheet(os);
        os.close();
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        table.readSpreadsheet(is);
        is.close();
        JFrame frame = makeFrame(getName(), table);
        maybeCloseFrame(frame);
    }
