    private void postData() throws IOException {
        try {
            URL url = new URL(baseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
            OutputStreamWriter solrWriter = new OutputStreamWriter(conn.getOutputStream());
            transformer.transform(new DOMSource(doc), new StreamResult(solrWriter));
            solrWriter.flush();
            solrWriter.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = rd.readLine()) != null) {
                if (debug) {
                    System.out.println(line);
                }
                if (outWriter != null) {
                    outWriter.write(line);
                    outWriter.write('\n');
                }
            }
            rd.close();
        } catch (TransformerException exc) {
            exc.printStackTrace();
        }
    }
