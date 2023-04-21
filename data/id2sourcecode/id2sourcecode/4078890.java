    public void submit(URL url) {
        try {
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-type", "application/zip");
            urlCon.setAllowUserInteraction(false);
            urlCon.setUseCaches(false);
            OutputStream out = urlCon.getOutputStream();
            CorpusXMLZipWriter cxzw = new CorpusXMLZipWriter(out);
            cxzw.write(_corpus);
            out.flush();
            out.close();
            urlCon.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String retVal = in.readLine();
            in.close();
            System.out.println("=======> Wrote zip file to the outputstream returned: " + retVal);
            _currentState = INVALID_STATE;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
