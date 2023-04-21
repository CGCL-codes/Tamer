    private void writeDoc(BufferedWriter bw, int docNum, Document doc, boolean decode) throws Exception {
        bw.write("<doc id='" + docNum + "'>\n");
        for (String fieldName : fieldNames) {
            Field[] fields = doc.getFields(fieldName);
            if (fields == null || fields.length == 0) {
                continue;
            }
            bw.write("<field name='" + Util.xmlEscape(fields[0].name()));
            if (reader.hasNorms(fields[0].name())) {
                bw.write("' norm='" + similarity.decodeNormValue(reader.norms(fields[0].name())[docNum]));
            }
            bw.write("' flags='" + Util.fieldFlags(fields[0]) + "'>\n");
            for (Field f : fields) {
                String val = null;
                if (decode) {
                    Decoder d = decoders.get(f.name());
                    if (d != null) {
                        val = d.decodeStored(f.name(), f);
                    }
                }
                if (!decode || val == null) {
                    if (f.isBinary()) {
                        val = Util.bytesToHex(f.getBinaryValue(), f.getBinaryOffset(), f.getBinaryLength(), false);
                    } else {
                        val = f.stringValue();
                    }
                }
                bw.write("<val>" + Util.xmlEscape(val) + "</val>\n");
            }
            TermFreqVector tfv = reader.getTermFreqVector(docNum, fieldName);
            if (tfv != null) {
                writeTermVector(bw, tfv);
            }
            bw.write("</field>\n");
        }
        bw.write("</doc>\n");
    }
