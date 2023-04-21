    public void testWriteSpreadsheet3() throws Exception {
        Spreadsheet table = new Spreadsheet(2, 2);
        table.setEditableByDefault(true);
        table.setCellAt("'\\u05d0'", 0, 0);
        table.setCellAt("\"\\u2021\"", 1, 1);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        table.writeSpreadsheet(os);
        os.close();
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        table.readSpreadsheet(is);
        is.close();
        JFrame frame = makeFrame(getName(), table);
        assertEquals(new Character('א'), table.getValueAt(0, 0));
        assertEquals("‡", table.getValueAt(1, 1));
        maybeCloseFrame(frame);
    }
