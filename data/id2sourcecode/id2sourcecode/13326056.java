    protected boolean handleStartElement(String localName, Attributes attributes) throws IOException, XMLStreamException {
        if (insideFieldValue && localName.equals("rPr")) {
            isInRPRBlock = true;
            this.styles.clear();
        } else if (insideFieldValue && isInRPRBlock) {
            String attrs = "";
            for (int i = 0; i < attributes.getCount(); i++) attrs += "w:" + attributes.getName(i) + "=" + "\"" + attributes.getValue(i) + "\"" + "  ";
            styles.add("<w:" + localName + " " + attrs + "/>");
        } else if (insideFieldValue && localName.equals("drawing")) {
            insideDrawing = true;
        }
        if (insideDrawing) {
            if (localName.equals("blip")) {
                String idImage = attributes.getValue("r:embed");
                String cstate = attributes.getValue("cstate");
                this.newIdImages.add(this.valueOfField);
                this.oldIdImages.add(idImage);
                this.writer.writeStartElement("a:blip");
                this.writer.writeAttribute("r:embed", this.valueOfField);
                if (cstate != null) this.writer.writeAttribute("cstate", cstate);
                return true;
            } else if (localName.equals("extent")) {
                handleTableField();
                int[] dimension = this.repository.getImageDimension(this.valueOfField);
                int cx = dimension[0] * 914400 / 96;
                int cy = dimension[1] * 914400 / 96;
                this.writer.writeStartElement("wp:extent");
                this.writer.writeAttribute("cx", Integer.toString(cx));
                this.writer.writeAttribute("cy", Integer.toString(cy));
                return true;
            } else if (localName.equals("ext")) {
                int[] dimension = this.repository.getImageDimension(this.valueOfField);
                int cx = dimension[0] * 914400 / 96;
                int cy = dimension[1] * 914400 / 96;
                this.writer.writeStartElement("a:ext");
                this.writer.writeAttribute("cx", Integer.toString(cx));
                this.writer.writeAttribute("cy", Integer.toString(cy));
                return true;
            }
            return false;
        }
        if (localName.equals("fldChar")) {
            String fldCharType = attributes.getValue("w:fldCharType");
            if (fldCharType.equals("begin")) {
                insideField = true;
                valueInserted = false;
            } else if (fldCharType.equals("separate")) {
                insideFieldValue = true;
                insideInstrText = false;
            } else if (fldCharType.equals("end")) {
                insideFieldValueContent = insideFieldValue = insideField = false;
                valueOfField = "";
            }
        } else if (localName.equals("fldSimple")) {
            fieldIdentifier.append(attributes.getValue("w:instr"));
            insideField = true;
            insideFieldValue = true;
            valueInserted = false;
            insideInstrText = false;
            this.writer.writeCharacters("");
            this.writer.flush();
            this.underlayingOutputStream.write(formatter.getFormatterString(Formatter.FIELD_BEGIN).getBytes("UTF-8"));
            this.underlayingOutputStream.write(fieldIdentifier.toString().getBytes("UTF-8"));
            this.underlayingOutputStream.write(formatter.getFormatterString(Formatter.FIELD_SEPARATE).getBytes("UTF-8"));
            return true;
        } else if (!localName.equals("fldChar") && insideFieldValueContent && valueInserted) {
            return true;
        } else if (localName.equals("name") && insideField) {
            ffDataName = attributes.getValue("w:val");
        } else if (localName.equalsIgnoreCase("checkbox")) {
            fieldType = OpenXMLFieldType.CHECKBOX;
        } else if (localName.equalsIgnoreCase("default") && fieldType == OpenXMLFieldType.CHECKBOX) {
            boolean checked = Boolean.parseBoolean(model.getPropertyAsString(ffDataName));
            String value = (checked) ? "1" : "0";
            if (ffDataName.length() > 0) attributes.setValue("w:val", value);
            valueOfField = value;
            valueInserted = true;
        } else if (localName.equals("instrText") && insideField) {
            insideInstrText = true;
        } else if (localName.equals("t") && insideFieldValue) {
            insideFieldValueContent = true;
        } else if (localName.equals("tbl")) {
            if (captureBlock && startBlock) {
                if (!showBlock) {
                    BlockProcessor blockProcessor = new BlockProcessor(this.reader, this.writer, this.underlayingOutputStream);
                    blockProcessor.setModel(this.model);
                    blockProcessor.setPartial(true);
                    blockProcessor.setRepository(repository);
                    blockProcessor.setDocumentId(documentId);
                    blockProcessor.showBlock(showBlock);
                    blockProcessor.setNamespceContext(this.getNamespaceContext());
                    blockProcessor.setNewIdImages(newIdImages);
                    blockProcessor.setOldIdImages(oldIdImages);
                    blockProcessor.Start();
                    startBlock = showBlock = captureBlock = false;
                    return true;
                }
                startBlock = showBlock = captureBlock = false;
                return false;
            } else if (captureTable) {
                TableProcessor tableProcessor = new TableProcessor(this.reader, this.writer, this.underlayingOutputStream);
                tableProcessor.setCollectionModel(currentCollection);
                tableProcessor.setPartial(true);
                tableProcessor.setRepository(repository);
                tableProcessor.setDocumentId(this.documentId);
                tableProcessor.setTableName(this.tableName);
                tableProcessor.setNamespceContext(this.getNamespaceContext());
                tableProcessor.setNewIdImages(newIdImages);
                tableProcessor.setOldIdImages(oldIdImages);
                tableProcessor.Start();
                captureTable = false;
                return true;
            }
            return false;
        } else if (localName.equals("_8E03AB25A2E342ea84854A32DEA84BBC")) {
            return true;
        } else if (captureBlock && startBlock) {
            if (showBlock) {
                BlockProcessor blockProcessor = new BlockProcessor(this.reader, this.writer, this.underlayingOutputStream);
                blockProcessor.setModel(this.model);
                blockProcessor.setPartial(true);
                blockProcessor.setRepository(repository);
                blockProcessor.setDocumentId(documentId);
                blockProcessor.setBlockName(this.blockName);
                blockProcessor.setNamespceContext(this.getNamespaceContext());
                blockProcessor.setNewIdImages(newIdImages);
                blockProcessor.setOldIdImages(oldIdImages);
                blockProcessor.Create();
            }
            startBlock = showBlock = captureBlock = false;
            return false;
        }
        return false;
    }
