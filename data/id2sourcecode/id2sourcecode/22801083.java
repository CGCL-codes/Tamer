    protected void parse(final URL url, final Uri feedId) {
        new Thread(new Runnable() {

            public void run() {
                Log.v("Parser", "parse feed...");
                try {
                    if (sp == null) sp = spf.newSAXParser();
                    InputSource is = new InputSource(url.openStream());
                    RssHandler handler = new RssHandler(UpdateFeed.this, feedId);
                    sp.parse(is, handler);
                    setResult(RESULT_OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        }).start();
    }
