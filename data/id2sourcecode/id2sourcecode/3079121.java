    public boolean setField(String name, String value, String display) throws IOException, DocumentException {
        if (writer == null) throw new DocumentException("This AcroFields instance is read-only.");
        Item item = (Item) fields.get(name);
        if (item == null) return false;
        PdfName type = (PdfName) PdfReader.getPdfObject(((PdfDictionary) item.merged.get(0)).get(PdfName.FT));
        if (PdfName.TX.equals(type)) {
            PdfNumber maxLen = (PdfNumber) PdfReader.getPdfObject(((PdfDictionary) item.merged.get(0)).get(PdfName.MAXLEN));
            int len = 0;
            if (maxLen != null) len = maxLen.intValue();
            if (len > 0) value = value.substring(0, Math.min(len, value.length()));
        }
        if (PdfName.TX.equals(type) || PdfName.CH.equals(type)) {
            PdfString v = new PdfString(value, PdfObject.TEXT_UNICODE);
            for (int idx = 0; idx < item.values.size(); ++idx) {
                ((PdfDictionary) item.values.get(idx)).put(PdfName.V, v);
                markUsed((PdfDictionary) item.values.get(idx));
                PdfDictionary merged = (PdfDictionary) item.merged.get(idx);
                merged.put(PdfName.V, v);
                PdfDictionary widget = (PdfDictionary) item.widgets.get(idx);
                if (generateAppearances) {
                    PdfAppearance app = getAppearance(merged, display, name);
                    if (PdfName.CH.equals(type)) {
                        PdfNumber n = new PdfNumber(topFirst);
                        widget.put(PdfName.TI, n);
                        merged.put(PdfName.TI, n);
                    }
                    PdfDictionary appDic = (PdfDictionary) PdfReader.getPdfObject(widget.get(PdfName.AP));
                    if (appDic == null) {
                        appDic = new PdfDictionary();
                        widget.put(PdfName.AP, appDic);
                        merged.put(PdfName.AP, appDic);
                    }
                    appDic.put(PdfName.N, app.getIndirectReference());
                } else {
                    widget.remove(PdfName.AP);
                    merged.remove(PdfName.AP);
                }
                markUsed(widget);
            }
            return true;
        } else if (PdfName.BTN.equals(type)) {
            PdfNumber ff = (PdfNumber) PdfReader.getPdfObject(((PdfDictionary) item.merged.get(0)).get(PdfName.FF));
            int flags = 0;
            if (ff != null) flags = ff.intValue();
            if ((flags & PdfFormField.FF_PUSHBUTTON) != 0) return true;
            PdfName v = new PdfName(value);
            if ((flags & PdfFormField.FF_RADIO) == 0) {
                for (int idx = 0; idx < item.values.size(); ++idx) {
                    ((PdfDictionary) item.values.get(idx)).put(PdfName.V, v);
                    markUsed((PdfDictionary) item.values.get(idx));
                    PdfDictionary merged = (PdfDictionary) item.merged.get(idx);
                    merged.put(PdfName.V, v);
                    merged.put(PdfName.AS, v);
                    PdfDictionary widget = (PdfDictionary) item.widgets.get(idx);
                    widget.put(PdfName.AS, v);
                    markUsed(widget);
                }
            } else {
                ArrayList lopt = new ArrayList();
                PdfObject opts = PdfReader.getPdfObject(((PdfDictionary) item.values.get(0)).get(PdfName.OPT));
                if (opts != null && opts.isArray()) {
                    ArrayList list = ((PdfArray) opts).getArrayList();
                    for (int k = 0; k < list.size(); ++k) {
                        PdfObject vv = PdfReader.getPdfObject((PdfObject) list.get(k));
                        if (vv != null && vv.isString()) lopt.add(((PdfString) vv).toUnicodeString()); else lopt.add(null);
                    }
                }
                int vidx = lopt.indexOf(value);
                PdfName valt = null;
                PdfName vt;
                if (vidx >= 0) {
                    vt = valt = new PdfName(String.valueOf(vidx));
                } else vt = v;
                for (int idx = 0; idx < item.values.size(); ++idx) {
                    PdfDictionary merged = (PdfDictionary) item.merged.get(idx);
                    PdfDictionary widget = (PdfDictionary) item.widgets.get(idx);
                    markUsed((PdfDictionary) item.values.get(idx));
                    if (valt != null) {
                        PdfString ps = new PdfString(value, PdfObject.TEXT_UNICODE);
                        ((PdfDictionary) item.values.get(idx)).put(PdfName.V, ps);
                        merged.put(PdfName.V, ps);
                    } else {
                        ((PdfDictionary) item.values.get(idx)).put(PdfName.V, v);
                        merged.put(PdfName.V, v);
                    }
                    markUsed(widget);
                    if (isInAP(widget, vt)) {
                        merged.put(PdfName.AS, vt);
                        widget.put(PdfName.AS, vt);
                    } else {
                        merged.put(PdfName.AS, PdfName.Off);
                        widget.put(PdfName.AS, PdfName.Off);
                    }
                }
            }
            return true;
        }
        return false;
    }
