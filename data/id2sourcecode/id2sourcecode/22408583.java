    public static void setXfa(XfaForm form, PdfReader reader, PdfWriter writer) throws IOException {
        PdfDictionary af = (PdfDictionary) PdfReader.getPdfObjectRelease(reader.getCatalog().get(PdfName.ACROFORM));
        if (af == null) {
            return;
        }
        PdfObject xfa = getXfaObject(reader);
        if (xfa.isArray()) {
            PdfArray ar = (PdfArray) xfa;
            int t = -1;
            int d = -1;
            for (int k = 0; k < ar.size(); k += 2) {
                PdfString s = ar.getAsString(k);
                if ("template".equals(s.toString())) {
                    t = k + 1;
                }
                if ("datasets".equals(s.toString())) {
                    d = k + 1;
                }
            }
            if (t > -1 && d > -1) {
                reader.killXref(ar.getAsIndirectObject(t));
                reader.killXref(ar.getAsIndirectObject(d));
                PdfStream tStream = new PdfStream(serializeDoc(form.templateNode));
                tStream.flateCompress(writer.getCompressionLevel());
                ar.set(t, writer.addToBody(tStream).getIndirectReference());
                PdfStream dStream = new PdfStream(serializeDoc(form.datasetsNode));
                dStream.flateCompress(writer.getCompressionLevel());
                ar.set(d, writer.addToBody(dStream).getIndirectReference());
                af.put(PdfName.XFA, new PdfArray(ar));
                return;
            }
        }
        reader.killXref(af.get(PdfName.XFA));
        PdfStream str = new PdfStream(serializeDoc(form.domDocument));
        str.flateCompress(writer.getCompressionLevel());
        PdfIndirectReference ref = writer.addToBody(str).getIndirectReference();
        af.put(PdfName.XFA, ref);
    }
