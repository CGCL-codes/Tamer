    private URL obtainExtraConfig(String pathlist) throws SAXException, IOException {
        StringTokenizer st = new StringTokenizer(pathlist, ",");
        while (st.hasMoreTokens()) {
            String path = st.nextToken().trim();
            StringTokenizer str = new StringTokenizer(path, INC_SEP);
            if (str.countTokens() != 2) configError(INCLUDEELEM + " with list needs the syntax " + INC_HREF + INC_SEP + "relativepath or " + INC_PATH + INC_SEP + "absolutepath", null);
            String type = str.nextToken();
            path = str.nextToken();
            URL url = null;
            if (INC_HREF.equals(type)) {
                url = getHrefURL(path);
                try {
                    url.openStream();
                    return url;
                } catch (IOException iox) {
                    configError("path error: ", iox);
                }
            } else if (INC_PATH.equals(type)) {
                if (new File(path).exists()) {
                    url = getPathURL(path);
                    return url;
                } else configError(INCLUDEELEM + ": " + path + " not found", null);
            }
        }
        return null;
    }
