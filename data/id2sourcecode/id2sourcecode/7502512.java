    static ArrayList<BioportalRow> getRowList() {
        if (_rows == null) try {
            _rows = new ArrayList<BioportalRow>();
            URL url = new URL(BioportalStrings.BASE);
            Document document = new SAXBuilder().build(url.openStream());
            if (document != null) parse(document);
        } catch (Exception ex) {
        }
        return _rows;
    }
