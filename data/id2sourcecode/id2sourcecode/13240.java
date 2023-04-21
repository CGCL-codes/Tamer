    private Document createDomDocument(final JTable jtable) {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            final Element rootElement = document.createElement("table");
            rootElement.setAttribute("Name", "TableName");
            document.appendChild(rootElement);
            final Element columnNames = document.createElement("header");
            for (int j = 0; j < jtable.getColumnCount(); j++) {
                final Element curElement = document.createElement("columnName");
                curElement.appendChild(document.createTextNode(jtable.getColumnName(j)));
                columnNames.appendChild(curElement);
            }
            rootElement.appendChild(columnNames);
            for (int i = 0; i < jtable.getRowCount(); i++) {
                final Element rowElement = document.createElement("row");
                for (int j = 0; j < jtable.getColumnCount(); j++) {
                    try {
                        final Object cellValue = jtable.getValueAt(i, j);
                        final String curValue = cellValue == null ? "" : cellValue.toString();
                        final Element curElement = document.createElement(jtable.getColumnName(j));
                        curElement.appendChild(document.createTextNode(curValue));
                        rowElement.appendChild(curElement);
                    } catch (DOMException exc) {
                        if (exc.code == DOMException.INVALID_CHARACTER_ERR) {
                            System.out.println("\nInvalid data: " + jtable.getColumnName(j) + " " + " " + exc.getMessage());
                        } else {
                            System.out.println("Unexpected error code of exception: " + exc);
                        }
                    }
                }
                rootElement.appendChild(rowElement);
            }
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException exc) {
            ConsoleFrame.addText("\n XmlWriter error:" + exc.getMessage());
        }
        return document;
    }
