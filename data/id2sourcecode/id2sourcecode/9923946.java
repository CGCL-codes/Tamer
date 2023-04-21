    private static void sendPostDownloadRequest(String endpoint, String outputdir, String outputfile, NodeCookie cookie) throws Exception {
        logger.debug("send download request " + endpoint);
        URL url = new URL(URLDecoder.decode(endpoint, "ISO-8859-1"));
        cookie.addCookieToUrl(url);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(outputdir + File.separator + outputfile));
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        NameSpaceDefinition ns = new NameSpaceDefinition();
        boolean found = false;
        while ((inputLine = reader.readLine()) != null) {
            if (!found) {
                Matcher m = NSPattern.matcher(inputLine);
                if (m.matches()) {
                    ns.init(m.group(1));
                    found = true;
                }
            }
            bw.write(inputLine + "\n");
        }
        bw.close();
        reader.close();
        cookie.storeCookie();
        XmlToJson.translateResultTable(outputdir + File.separator + outputfile, outputdir + File.separator + "result.json");
    }
