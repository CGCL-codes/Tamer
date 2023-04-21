    public boolean setFieldProperty(String field, String name, int value, int inst[]) {
        if (writer == null) throw new RuntimeException("This AcroFields instance is read-only.");
        Item item = (Item) fields.get(field);
        if (item == null) return false;
        InstHit hit = new InstHit(inst);
        if (name.equalsIgnoreCase("flags")) {
            PdfNumber num = new PdfNumber(value);
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    ((PdfDictionary) item.merged.get(k)).put(PdfName.F, num);
                    ((PdfDictionary) item.widgets.get(k)).put(PdfName.F, num);
                    markUsed((PdfDictionary) item.widgets.get(k));
                }
            }
        } else if (name.equalsIgnoreCase("setflags")) {
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfNumber num = (PdfNumber) PdfReader.getPdfObject(((PdfDictionary) item.widgets.get(k)).get(PdfName.F));
                    int val = 0;
                    if (num != null) val = num.intValue();
                    num = new PdfNumber(val | value);
                    ((PdfDictionary) item.merged.get(k)).put(PdfName.F, num);
                    ((PdfDictionary) item.widgets.get(k)).put(PdfName.F, num);
                    markUsed((PdfDictionary) item.widgets.get(k));
                }
            }
        } else if (name.equalsIgnoreCase("clrflags")) {
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfNumber num = (PdfNumber) PdfReader.getPdfObject(((PdfDictionary) item.widgets.get(k)).get(PdfName.F));
                    int val = 0;
                    if (num != null) val = num.intValue();
                    num = new PdfNumber(val & (~value));
                    ((PdfDictionary) item.merged.get(k)).put(PdfName.F, num);
                    ((PdfDictionary) item.widgets.get(k)).put(PdfName.F, num);
                    markUsed((PdfDictionary) item.widgets.get(k));
                }
            }
        } else if (name.equalsIgnoreCase("fflags")) {
            PdfNumber num = new PdfNumber(value);
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    ((PdfDictionary) item.merged.get(k)).put(PdfName.FF, num);
                    ((PdfDictionary) item.values.get(k)).put(PdfName.FF, num);
                    markUsed((PdfDictionary) item.values.get(k));
                }
            }
        } else if (name.equalsIgnoreCase("setfflags")) {
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfNumber num = (PdfNumber) PdfReader.getPdfObject(((PdfDictionary) item.values.get(k)).get(PdfName.FF));
                    int val = 0;
                    if (num != null) val = num.intValue();
                    num = new PdfNumber(val | value);
                    ((PdfDictionary) item.merged.get(k)).put(PdfName.FF, num);
                    ((PdfDictionary) item.values.get(k)).put(PdfName.FF, num);
                    markUsed((PdfDictionary) item.values.get(k));
                }
            }
        } else if (name.equalsIgnoreCase("clrfflags")) {
            for (int k = 0; k < item.merged.size(); ++k) {
                if (hit.isHit(k)) {
                    PdfNumber num = (PdfNumber) PdfReader.getPdfObject(((PdfDictionary) item.values.get(k)).get(PdfName.FF));
                    int val = 0;
                    if (num != null) val = num.intValue();
                    num = new PdfNumber(val & (~value));
                    ((PdfDictionary) item.merged.get(k)).put(PdfName.FF, num);
                    ((PdfDictionary) item.values.get(k)).put(PdfName.FF, num);
                    markUsed((PdfDictionary) item.values.get(k));
                }
            }
        } else return false;
        return true;
    }
