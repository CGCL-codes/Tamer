    public CheckoutResponse send() {
        try {
            URL url = new URL(getPostUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + merchantConstants.getHttpAuth());
            connection.setRequestProperty("Host", connection.getURL().getHost());
            connection.setRequestProperty("content-type", "application/xml; charset=UTF-8");
            connection.setRequestProperty("accept", "application/xml");
            PrintWriter output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF8"));
            output.print(getXml());
            output.flush();
            output.close();
            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            InputStream inputStream;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = ((HttpURLConnection) connection).getInputStream();
            } else {
                inputStream = ((HttpURLConnection) connection).getErrorStream();
            }
            return new CheckoutResponse(inputStream);
        } catch (MalformedURLException murle) {
            System.err.println("MalformedURLException encountered.");
        } catch (IOException ioe) {
            System.err.println("IOException encountered.");
        }
        return null;
    }
