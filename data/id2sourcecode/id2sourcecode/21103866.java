    public void beginSheet() throws IOException {
        _out.write("<?xml version=\"1.0\" encoding=\"" + xmlEncoding + "\"?>" + "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        if (!columnWidthMap.isEmpty()) {
            _out.write("<cols>");
            Set keySet = columnWidthMap.keySet();
            Iterator iterator = keySet.iterator();
            while (iterator.hasNext()) {
                Integer col = (Integer) iterator.next();
                _out.write("<col min=\"" + col + "\" max=\"" + col + "\" width=\"" + columnWidthMap.get(col) + "\" customWidth=\"1\"/>");
            }
            _out.write("</cols>");
        }
        _out.write("<sheetData>\n");
    }
