        @Override
        public void run() {
            AndroidHttpClient client = null;
            try {
                URI uri = new URI("http", null, "www.google.com", -1, "/books", "vid=isbn" + mISBN + "&jscmd=SearchWithinVolume2&q=" + mQuery, null);
                HttpUriRequest get = new HttpGet(uri);
                get.setHeader("cookie", getCookie(uri.toString()));
                client = AndroidHttpClient.newInstance(USER_AGENT);
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    ByteArrayOutputStream jsonHolder = new ByteArrayOutputStream();
                    entity.writeTo(jsonHolder);
                    jsonHolder.flush();
                    JSONObject json = new JSONObject(jsonHolder.toString(getEncoding(entity)));
                    jsonHolder.close();
                    Message message = Message.obtain(mHandler, R.id.search_book_contents_succeeded);
                    message.obj = json;
                    message.sendToTarget();
                } else {
                    Log.e(TAG, "HTTP returned " + response.getStatusLine().getStatusCode() + " for " + uri);
                    Message message = Message.obtain(mHandler, R.id.search_book_contents_failed);
                    message.sendToTarget();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                Message message = Message.obtain(mHandler, R.id.search_book_contents_failed);
                message.sendToTarget();
            } finally {
                if (client != null) {
                    client.close();
                }
            }
        }
