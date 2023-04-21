    public int openChangeset(final Collection<Tag> aComments, final String aUserName, final String aPassword) throws IOException {
        URL url = new URL(Settings.getInstance().get(APIBASEURSETTING, DEFAULTAPIBASEURL) + "/changeset/create");
        System.err.println("DEBUG: URL= " + url.toString());
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestProperty("Authorization", "Basic " + (new sun.misc.BASE64Encoder()).encode((aUserName + ":" + aPassword).getBytes()));
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("<osm version=\"0.6\" generator=\"Traveling Salesman - LibOSM\">\n\t<changeset>\n");
        for (Tag tag : aComments) {
            out.write("\t\t<tag k=\"" + tag.getKey() + "\" v=\"" + tag.getValue() + "\"/>\n");
        }
        out.write("\t</changeset>\n</osm>");
        out.close();
        int responseCode = httpCon.getResponseCode();
        LOG.info("response-code to opening changeset: " + responseCode);
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IllegalStateException("Http-Status-code is not 200 but" + httpCon.getResponseCode() + " \"" + httpCon.getResponseMessage() + "\" Error=" + httpCon.getHeaderField("Error"));
        }
        InputStreamReader in = new InputStreamReader(httpCon.getInputStream());
        char[] buffer = new char[Byte.MAX_VALUE];
        int len = in.read(buffer);
        int changeset = Integer.parseInt(new String(buffer, 0, len));
        System.err.println("DEBUG: changeset= " + changeset);
        return changeset;
    }
