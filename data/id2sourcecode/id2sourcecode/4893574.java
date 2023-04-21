    public DocumentsWriter.DocWriter processDocument() throws IOException {
        consumer.startDocument();
        final Document doc = docState.doc;
        assert docFieldProcessor.docWriter.writer.testPoint("DocumentsWriter.ThreadState.init start");
        fieldCount = 0;
        final int thisFieldGen = fieldGen++;
        final List docFields = doc.getFields();
        final int numDocFields = docFields.size();
        for (int i = 0; i < numDocFields; i++) {
            Fieldable field = (Fieldable) docFields.get(i);
            final String fieldName = field.name();
            final int hashPos = fieldName.hashCode() & hashMask;
            DocFieldProcessorPerField fp = fieldHash[hashPos];
            while (fp != null && !fp.fieldInfo.name.equals(fieldName)) fp = fp.next;
            if (fp == null) {
                FieldInfo fi = fieldInfos.add(fieldName, field.isIndexed(), field.isTermVectorStored(), field.isStorePositionWithTermVector(), field.isStoreOffsetWithTermVector(), field.getOmitNorms(), false, field.getOmitTf());
                fp = new DocFieldProcessorPerField(this, fi);
                fp.next = fieldHash[hashPos];
                fieldHash[hashPos] = fp;
                totalFieldCount++;
                if (totalFieldCount >= fieldHash.length / 2) rehash();
            } else fp.fieldInfo.update(field.isIndexed(), field.isTermVectorStored(), field.isStorePositionWithTermVector(), field.isStoreOffsetWithTermVector(), field.getOmitNorms(), false, field.getOmitTf());
            if (thisFieldGen != fp.lastGen) {
                fp.fieldCount = 0;
                if (fieldCount == fields.length) {
                    final int newSize = fields.length * 2;
                    DocFieldProcessorPerField newArray[] = new DocFieldProcessorPerField[newSize];
                    System.arraycopy(fields, 0, newArray, 0, fieldCount);
                    fields = newArray;
                }
                fields[fieldCount++] = fp;
                fp.lastGen = thisFieldGen;
            }
            if (fp.fieldCount == fp.fields.length) {
                Fieldable[] newArray = new Fieldable[fp.fields.length * 2];
                System.arraycopy(fp.fields, 0, newArray, 0, fp.fieldCount);
                fp.fields = newArray;
            }
            fp.fields[fp.fieldCount++] = field;
        }
        quickSort(fields, 0, fieldCount - 1);
        for (int i = 0; i < fieldCount; i++) fields[i].consumer.processFields(fields[i].fields, fields[i].fieldCount);
        if (docState.maxTermPrefix != null && docState.infoStream != null) docState.infoStream.println("WARNING: document contains at least one immense term (longer than the max length " + DocumentsWriter.MAX_TERM_LENGTH + "), all of which were skipped.  Please correct the analyzer to not produce such terms.  The prefix of the first immense term is: '" + docState.maxTermPrefix + "...'");
        return consumer.finishDocument();
    }
