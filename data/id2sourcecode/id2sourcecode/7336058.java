    private IReply recurseGet(URL url, int retry) throws IOException {
        RawReply rep = (RawReply) processReply((HttpURLConnection) url.openConnection());
        if (rep.getSuccess() && (rep.getHTTPStatusCode() == HttpURLConnection.HTTP_OK)) {
            return rep;
        } else if ((rep.getHTTPStatusCode() == HttpURLConnection.HTTP_UNAVAILABLE) || (rep.getHTTPStatusCode() == HttpURLConnection.HTTP_INTERNAL_ERROR)) {
            if (retry < this.maxRetries) {
                try {
                    Thread.sleep(((retry ^ 2) * 75) + 50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return recurseGet(url, retry++);
            }
        }
        return rep;
    }
