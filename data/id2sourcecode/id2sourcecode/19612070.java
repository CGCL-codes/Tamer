    public void processFields(Fieldable[] fields, int count) throws IOException {
        final StoredFieldsWriter.PerDoc doc;
        if (perThread.doc == null) {
            doc = perThread.doc = perThread.storedFieldsWriter.getPerDoc();
            doc.docID = docState.docID;
            perThread.localFieldsWriter.setFieldsStream(doc.fdt);
            assert doc.numStoredFields == 0 : "doc.numStoredFields=" + doc.numStoredFields;
            assert 0 == doc.fdt.length();
            assert 0 == doc.fdt.getFilePointer();
        } else {
            doc = perThread.doc;
            assert doc.docID == docState.docID : "doc.docID=" + doc.docID + " docState.docID=" + docState.docID;
        }
        for (int i = 0; i < count; i++) {
            final Fieldable field = fields[i];
            if (field.isStored()) {
                perThread.localFieldsWriter.writeField(fieldInfo, field);
                assert docState.testPoint("StoredFieldsWriterPerField.processFields.writeField");
                doc.numStoredFields++;
            }
        }
    }
