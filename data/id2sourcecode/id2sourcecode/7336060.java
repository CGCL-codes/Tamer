    private IReply recursePost(URL url, String postBody, int retry) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(con.getOutputStream(), 8192));
        writer.write(postBody);
        writer.close();
        RawReply rep = (RawReply) processReply(con);
        if (rep.getSuccess() && (rep.getHTTPStatusCode() == HttpURLConnection.HTTP_OK)) {
            return rep;
        } else if ((rep.getHTTPStatusCode() == HttpURLConnection.HTTP_UNAVAILABLE) || (rep.getHTTPStatusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)) {
            if (retry < this.maxRetries) {
                try {
                    Thread.sleep(((retry ^ 2) * 75) + 50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return recursePost(url, postBody, retry++);
            }
        }
        return rep;
    }
