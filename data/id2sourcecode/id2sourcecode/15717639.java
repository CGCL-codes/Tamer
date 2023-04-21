    public final void readAnnots(String annots) throws PdfException {
        LogWriter.writeMethod("{pdf-readAnnots: " + annots + "}", 1);
        annots = Strip.removeArrayDeleminators(annots);
        if (annots.length() > 0) {
            try {
                if (annots.startsWith("<<")) {
                } else {
                    StringTokenizer initialValues = new StringTokenizer(annots, "R");
                    while (initialValues.hasMoreTokens()) {
                        String value = initialValues.nextToken().trim() + " R";
                        readAnnot(value);
                    }
                }
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " processing annots >" + annots + "<");
                throw new PdfException("Exception");
            }
        }
    }
