    private final void readAnnot(String annot) {
        LogWriter.writeMethod("{pdf-readAnnot: " + annot + "}", 1);
        Map annotationObject = new Hashtable();
        if (annot.startsWith("<<")) annotationObject = currentPdfFile.directValuesToMap(annot); else {
            annotationObject = currentPdfFile.readObject(annot, false, fields);
        }
        String type = (String) annotationObject.get("Type");
        if (type == null) type = "/Annot";
        String subtype = (String) annotationObject.get("Subtype");
        if (subtype == null) subtype = "none";
        annotType.put(new Integer(annotNumber), subtype);
        if (annotationObject.get("AP") != null) annotCustomIcon.put(new Integer(annotNumber), "x");
        String rectString = (String) annotationObject.get("Rect");
        if (rectString != null) {
            String rect = Strip.removeArrayDeleminators(currentPdfFile.getValue(rectString));
            annotArea.put(new Integer(annotNumber), rect);
            String[] keysToSubstitute = { "A", "FS" };
            substituteKeyValues(currentPdfFile, annotationObject, keysToSubstitute);
            this.Annots.add(annotNumber, annotationObject);
            annotNumber++;
        }
    }
