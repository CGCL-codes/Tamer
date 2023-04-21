    public void beginSheet() throws IOException {
        _out.write("<?xml version=\"1.0\" encoding=\"" + XML_ENCODING + "\"?>" + "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        _out.write("<sheetData>\n");
    }
