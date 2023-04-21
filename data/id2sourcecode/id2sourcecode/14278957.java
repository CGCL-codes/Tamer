        public void writeSpreadsheet(OutputStream os) throws IOException {
            try {
                StreamResult stream = new StreamResult(os);
                _handler.setResult(stream);
                _handler.startDocument();
                AttributesImpl atts = new AttributesImpl();
                int rows = getRowCount();
                int cols = getColumnCount();
                atts.clear();
                atts.addAttribute("", "", "vers", "", "0.1.0");
                atts.addAttribute("", "", "rows", "", String.valueOf(rows));
                atts.addAttribute("", "", "cols", "", String.valueOf(cols));
                Class<?> defaultType = getDefaultCellType();
                if (!defaultType.equals(Integer.class)) {
                    atts.addAttribute("", "", "defaultType", "", defaultType.getName());
                }
                Object defaultValue = getDefaultCellValue();
                if (!defaultValue.equals(0)) {
                    atts.addAttribute("", "", "defaultValue", "", getDefaultCellValue().toString());
                }
                _handler.startElement("", "", "hacksheet", atts);
                _cellsWritten = new HashSet<Cell>(_model._cellmap.values().size() * 4 / 3);
                writeCells(_model._cellmap.values().iterator());
                _handler.endElement("", "", "hacksheet");
                _handler.endDocument();
            } catch (SAXException x) {
                IOException iox = new IOException(x.getMessage());
                iox.initCause(x);
                throw iox;
            }
        }
