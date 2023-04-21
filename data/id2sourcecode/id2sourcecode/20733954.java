    public static final void upload(final String username, final String password, final String description, final String tags, final File gpxFile, final String visibility) throws IOException {
        Generic.debug("uploading " + gpxFile.getAbsolutePath() + " to openstreetmap.org");
        try {
            final String urlDesc = description.length() == 0 ? "No description" : description.replaceAll("\\.;&?,/", "_");
            final String urlTags = tags.replaceAll("\\\\.;&?,/", "_");
            final URL url = new URL("http://www.openstreetmap.org/api/" + API_VERSION + "/gpx/create");
            Generic.debug("url: " + url);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(15000);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.addRequestProperty("Authorization", "Basic " + encodeBase64(username + ":" + password));
            con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            con.addRequestProperty("Connection", "close");
            con.addRequestProperty("Expect", "");
            con.connect();
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(con.getOutputStream()));
            writeContentDispositionFile(out, "file", gpxFile);
            writeContentDisposition(out, "description", urlDesc);
            writeContentDisposition(out, "tags", urlTags);
            writeContentDisposition(out, "visibility", visibility);
            out.writeBytes("--" + BOUNDARY + "--" + LINE_END);
            out.flush();
            final int retCode = con.getResponseCode();
            String retMsg = con.getResponseMessage();
            Generic.debug("\nreturn code: " + retCode + " " + retMsg);
            if (retCode != 200) {
                if (con.getHeaderField("Error") != null) {
                    retMsg += "\n" + con.getHeaderField("Error");
                }
                con.disconnect();
                throw new RuntimeException(retCode + " " + retMsg);
            }
            out.close();
            con.disconnect();
            Generic.debug(gpxFile.getAbsolutePath());
        } catch (UnsupportedEncodingException ignore) {
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
