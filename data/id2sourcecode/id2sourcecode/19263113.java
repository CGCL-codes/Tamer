    public StringBuilder getTag(String firstTag, String endTag) throws IOException {
        URL url = new URL(loginURL);
        URLConnection urlConnect = url.openConnection();
        BufferedReader buf = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
        StringBuilder strB = new StringBuilder();
        String input = null;
        while ((input = buf.readLine()) != null) strB.append(input);
        int firstTagPos = strB.indexOf(firstTag);
        int endTagPos = strB.indexOf(endTag);
        if (endTagPos > 0) {
            strB = new StringBuilder(strB.substring(firstTagPos, endTagPos));
        }
        buf.close();
        return strB;
    }
