    private void PostData(Reader data) throws NpsException {
        if (Config.SOLR_URL == null) return;
        HttpURLConnection urlc = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            if (solrUrl == null) solrUrl = new URL(Config.SOLR_URL + "/" + solrCore + "/update");
            urlc = (HttpURLConnection) solrUrl.openConnection();
            urlc.setRequestMethod("POST");
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml;charset=UTF-8");
            out = urlc.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            pipe(data, writer);
            try {
                writer.close();
            } catch (Exception e1) {
            }
            in = urlc.getInputStream();
            StringWriter output = new StringWriter();
            Reader reader = new InputStreamReader(in, "UTF-8");
            pipe(reader, output);
            try {
                reader.close();
            } catch (Exception e1) {
            }
            String SOLR_OK_RESPONSE_EXCERPT = "<int name=\"status\">0</int>";
            if (output.toString().indexOf(SOLR_OK_RESPONSE_EXCERPT) < 0) {
                nps.util.DefaultLog.error(output.toString(), ErrorHelper.INDEX_POST_ERROR);
            }
        } catch (NpsException e) {
            throw e;
        } catch (Exception e) {
            DefaultLog.error(e, ErrorHelper.INDEX_POST_ERROR);
        } finally {
            if (in != null) try {
                in.close();
            } catch (Exception e1) {
            }
            if (out != null) try {
                out.close();
            } catch (Exception e1) {
            }
            if (urlc != null) urlc.disconnect();
        }
    }
