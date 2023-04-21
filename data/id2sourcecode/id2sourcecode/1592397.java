    protected boolean handleStartElement(String localName, Attributes attributes) throws IOException, XMLStreamException {
        if (localName.equals("user-field-decls")) {
            this.bInsideUserFieldDecls = true;
        } else if (localName.equals("user-field-decl") && this.bInsideUserFieldDecls) {
            String sName = attributes.getValue("text:name");
            if (sName.startsWith(TABLE)) {
                bIgnoreEndElement = processFieldDecl(sName);
                return bIgnoreEndElement;
            } else if (this.model.isPropertyAString(sName)) {
                writeProperty(localName, sName);
                bIgnoreEndElement = false;
                return true;
            }
        } else if (localName.equals("user-field-get")) {
            this.bIgnoreContent = true;
            String fieldName = attributes.getValue("text:name");
            if (!detectTableField(fieldName, attributes)) {
                Matcher matcher = TABLE_CELL_FIELD_PATTERN.matcher(fieldName);
                if (matcher.find()) {
                    writeUserFieldGet(localName, fieldName.replaceFirst("ROW.(\\w*).CELL", String.format("ROW.%d.CELL", rowIndex)));
                    return true;
                }
            }
        } else if (localName.equals("radio")) {
            String fieldName = attributes.getValue("form:name");
            String label = attributes.getValue("form:label");
            if (this.model.isPropertyAString(label)) {
                writeProperty(fieldName, label);
                addProperty("current-selected", "true", fieldName, label);
                addProperty("selected", "true", fieldName, label);
            }
            return true;
        } else if (localName.equals("table") && captureTable) {
            TableProcessor tableProcessor = new TableProcessor(this.reader, this.writer, this.underlayingOutputStream);
            tableProcessor.setCollectionModel(currentCollection);
            tableProcessor.setPartial(true);
            tableProcessor.setNamespceContext(this.getNamespaceContext());
            tableProcessor.Start();
            captureTable = false;
            return true;
        } else if (localName.equals("_8E03AB25A2E342ea84854A32DEA84BBC")) {
            return true;
        }
        return false;
    }
