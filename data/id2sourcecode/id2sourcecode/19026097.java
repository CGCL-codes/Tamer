    public static CMLUnitTypeList createUnitTypeList(URL url) throws IOException, CMLException {
        Document dictDoc = null;
        InputStream in = null;
        try {
            in = url.openStream();
            dictDoc = new CMLBuilder().build(in);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new CMLException("NULL " + e.getMessage() + S_SLASH + e.getCause() + " in " + url);
        } catch (ValidityException e) {
            throw new CMLException(S_EMPTY + e.getMessage() + S_SLASH + e.getCause() + " in " + url);
        } catch (ParsingException e) {
            throw new CMLException("PARSE " + S_EMPTY + e.getMessage() + " in " + url);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CMLUnitTypeList dt = null;
        if (dictDoc != null) {
            Element root = dictDoc.getRootElement();
            if (root instanceof CMLUnitTypeList) {
                dt = new CMLUnitTypeList((CMLUnitTypeList) root);
            } else {
            }
        }
        if (dt != null) {
            dt.indexEntries();
        }
        return dt;
    }
