    private void parse() throws Exception {
        url = new URL(apiUrl);
        is = url.openStream();
        try {
            xmlDocument = builder.parse(is);
            root = xmlDocument.getDocumentElement();
            items = root.getElementsByTagName(dataField);
        } catch (Exception e) {
            errors.add("API검색을 할수 없습니다. error - parseData from inputstream.");
            Malgn.errorLog("{Generator.parse} " + e.getMessage());
        } finally {
            if (is != null) is.close();
        }
    }
