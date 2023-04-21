    public String httpRequestByGET(String url, int timeout) {
        String response = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpParams params = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, timeout);
            HttpConnectionParams.setSoTimeout(params, timeout);
            HttpResponse serverResponse = httpClient.execute(new HttpGet(url));
            StatusLine statusLine = serverResponse.getStatusLine();
            if (statusLine.getStatusCode() < 400) {
                InputStream inputStream = serverResponse.getEntity().getContent();
                InputStreamReader inputReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputReader);
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }
                response = buffer.toString();
                inputStream.close();
            } else {
                this.lastErrorCode = String.valueOf(statusLine.getStatusCode());
                this.lastErrorMessage = statusLine.getReasonPhrase();
                return null;
            }
        } catch (IOException e) {
            this.lastErrorCode = "-1";
            this.lastErrorMessage = e.getLocalizedMessage();
            return null;
        }
        return response;
    }
