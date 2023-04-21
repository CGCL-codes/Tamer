    public static IndexableList createFrom(URL url, Class indexableListClass) {
        IndexableList indexableList = null;
        if ("file".equals(url.getProtocol())) {
            File file;
            try {
                file = new File(url.toURI());
                if (file.isDirectory()) {
                    indexableList = createFromDirectory(file, indexableListClass);
                } else if (file.toString().endsWith(XML_SUFF)) {
                    try {
                        indexableList = (IndexableList) new CMLBuilder().build(file).getRootElement();
                    } catch (Exception e) {
                        throw new CMLRuntimeException("Cannot parse " + file, e);
                    }
                } else {
                    throw new CMLRuntimeException("exptected either a directory ot *.xml; found: " + url);
                }
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
        } else {
            if (url.toExternalForm().endsWith(XML_SUFF)) {
                InputStream in = null;
                try {
                    in = url.openStream();
                    indexableList = (IndexableList) new CMLBuilder().build(in).getRootElement();
                } catch (ValidityException e) {
                    throw new CMLRuntimeException("Problem parsing " + url + " as a CML document: " + e.getMessage(), e);
                } catch (ParsingException e) {
                    throw new CMLRuntimeException("Problem parsing " + url + " as a CML document: " + e.getMessage(), e);
                } catch (IOException e) {
                    throw new CMLRuntimeException("Problem parsing " + url + " as a CML document: " + e.getMessage(), e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                }
            } else {
                throw new CMLRuntimeException("Don't know how to get referenced document(s) from : " + url);
            }
        }
        return indexableList;
    }
