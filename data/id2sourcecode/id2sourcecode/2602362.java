    protected boolean handleStartElement(String localName, Attributes attributes) throws IOException, XMLStreamException {
        if (localName.equals("fldChar")) {
            String fldCharType = attributes.getValue("w:fldCharType");
            if (fldCharType.equals("begin")) {
                insideField = true;
                valueInserted = false;
            } else if (fldCharType.equals("separate")) {
                insideFieldValue = true;
                insideInstrText = false;
            } else if (fldCharType.equals("end")) {
                insideFieldValue = insideField = false;
                valueOfField = null;
            }
        } else if (localName.equals("fldSimple")) {
            fieldIdentifier.append(attributes.getValue("w:instr"));
            insideField = true;
            insideFieldValue = true;
            valueInserted = false;
            insideInstrText = false;
        } else if (localName.equals("instrText") && insideField) {
            insideInstrText = true;
        } else if (localName.equals("t") && insideFieldValue) {
            insideFieldValueContent = true;
        } else if (localName.equals("tbl")) {
            if (captureTable) {
                TableProcessor tableProcessor = new TableProcessor(this.reader, this.writer, this.underlayingOutputStream);
                tableProcessor.setPartial(true);
                tableProcessor.setNamespceContext(this.getNamespaceContext());
                tableProcessor.setRepository(this.repository);
                tableProcessor.setTableName(this.currentTableName);
                tableProcessor.setTemplateId(this.templateId);
                tableProcessor.Start();
                this.captureTable = false;
                this.currentTableName = null;
                return true;
            } else if (captureBlock) {
                BlockProcessor blockProcessor = new BlockProcessor(this.reader);
                blockProcessor.setPartial(true);
                blockProcessor.setNamespceContext(this.getNamespaceContext());
                blockProcessor.setRepository(this.repository);
                blockProcessor.setBlockName(this.currentBlockName);
                blockProcessor.setTemplateId(this.templateId);
                blockProcessor.Start();
                this.captureBlock = false;
                this.currentBlockName = null;
            }
        } else if (localName.equals("_8E03AB25A2E342ea84854A32DEA84BBC")) {
            return true;
        }
        return false;
    }
