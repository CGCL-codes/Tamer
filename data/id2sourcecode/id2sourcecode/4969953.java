    public boolean connectToUrl(String url_address) {
        message = new StringBuffer("");
        try {
            URL url = new URL(url_address);
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setDoOutput(false);
                httpConnection.connect();
                message.append("<BR>\n Connection Code:[" + httpConnection.getResponseCode() + "]");
                message.append("<BR>\n Response Message:[" + httpConnection.getResponseMessage() + "]");
                InputStreamReader insr = new InputStreamReader(httpConnection.getInputStream());
                BufferedReader in = new BufferedReader(insr);
                fullStringBuffer = new StringBuffer("");
                String temp = in.readLine();
                while (temp != null) {
                    fullStringBuffer.append(temp + "\n");
                    temp = in.readLine();
                }
                in.close();
            } catch (IOException e) {
                message.append("<BR>\n [Error][IOException][" + e.getMessage() + "]");
                return false;
            }
        } catch (MalformedURLException e) {
            message.append("<BR>\n [Error][MalformedURLException][" + e.getMessage() + "]");
            return false;
        } catch (Exception e) {
            message.append("<BR>\n [Error][Exception][" + e.getMessage() + "]");
            return false;
        }
        return true;
    }
