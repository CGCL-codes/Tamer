    public void testWriteSpreadsheet() throws Exception {
        Spreadsheet table = new Spreadsheet(3, 3);
        table.setEditableByDefault(true);
        table.setCellAt("1", 0, 0);
        table.setCellName("c1", 0, 0);
        table.setCellAt("\"foo\"", 0, 1);
        table.setCellAt("cell(0,1).length()", 0, 2);
        table.setCellAt("2", 1, 0);
        table.setCellName("c2", 1, 0);
        table.setCellAt("Math.PI", 1, 1);
        table.setCellAt("new java.awt.Point()", 1, 2);
        table.setCellAt("cell(\"c1\") + cell(\"c2\")", 2, 0, false);
        table.setCellAt("new java.util.Date()", 2, 1);
        table.setCellAt("java.awt.Color.ORANGE", 2, 2);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        table.writeSpreadsheet(os);
        os.close();
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        table.readSpreadsheet(is);
        is.close();
        JFrame frame = makeFrame(getName(), table);
        maybeCloseFrame(frame);
    }
