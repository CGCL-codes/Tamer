        @Override
        public void run() {
            AndroidHttpClient client = null;
            try {
                URI uri;
                if (isbn.startsWith("http://google.com/books?id=")) {
                    int equals = isbn.indexOf('=');
                    String volumeId = isbn.substring(equals + 1);
                    uri = new URI("http", null, "www.google.com", -1, "/books", "id=" + volumeId + "&jscmd=SearchWithinVolume2&q=" + query, null);
                } else {
                    uri = new URI("http", null, "www.google.com", -1, "/books", "vid=isbn" + isbn + "&jscmd=SearchWithinVolume2&q=" + query, null);
                }
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
                    Message message = Message.obtain(handler, R.id.search_book_contents_succeeded);
                    message.obj = json;
                    message.sendToTarget();
                } else {
                    Log.w(TAG, "HTTP returned " + response.getStatusLine().getStatusCode() + " for " + uri);
                    Message message = Message.obtain(handler, R.id.search_book_contents_failed);
                    message.sendToTarget();
                }
            } catch (Exception e) {
                Log.w(TAG, "Error accessing book search", e);
                Message message = Message.obtain(handler, R.id.search_book_contents_failed);
                message.sendToTarget();
            } finally {
                if (client != null) {
                    client.close();
                }
            }
        }
