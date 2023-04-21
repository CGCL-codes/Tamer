    @Override
    public Bitmap doInBackground(String... values) {
        String url = values[0];
        AndroidHttpClient client = AndroidHttpClient.newInstance(mUserAgent);
        HttpGet request = new HttpGet(url);
        HttpClientParams.setRedirecting(client.getParams(), true);
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream content = entity.getContent();
                    if (content != null) {
                        Bitmap icon = BitmapFactory.decodeStream(content, null, null);
                        return icon;
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            request.abort();
        } catch (IOException ex) {
            request.abort();
        } finally {
            client.close();
        }
        return null;
    }
