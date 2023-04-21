    protected static String getSourceLine(String sourceUrl, int lineNum) throws Exception {
        URL url = null;
        try {
            url = new URL(sourceUrl);
        } catch (java.net.MalformedURLException mue) {
            int indexOfColon = sourceUrl.indexOf(':');
            int indexOfSlash = sourceUrl.indexOf('/');
            if ((indexOfColon != -1) && (indexOfSlash != -1) && (indexOfColon < indexOfSlash)) {
                throw mue;
            } else {
                url = new URL(SystemIDResolver.getAbsoluteURI(sourceUrl));
            }
        }
        String line = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            URLConnection uc = url.openConnection();
            is = uc.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            for (int i = 1; i <= lineNum; i++) {
                line = br.readLine();
            }
        } finally {
            br.close();
            is.close();
        }
        return line;
    }
