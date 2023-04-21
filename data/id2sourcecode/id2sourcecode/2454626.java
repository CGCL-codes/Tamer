    public static void write(final String title, final TableModel model, final Content.TableModelFilter filter, final OutputStream output) throws IOException {
        DocumentWriter writer = null;
        try {
            writer = new SpreadsheetWriter(output);
            Content content = writer.beginDefaultContent();
            content.beginTable(title);
            content.addTableModel(model, filter);
            content.endTable();
        } finally {
            FS.close(writer);
        }
    }
