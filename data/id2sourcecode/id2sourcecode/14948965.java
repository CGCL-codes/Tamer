        public void run() {
            myRssFeed = null;
            while (threadnr != 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            threadnr++;
            String currentUrl = rssUrl;
            try {
                URL url = new URL(currentUrl);
                RSSHandler myRSSHandler = new RSSHandler(context);
                myXMLReader.setContentHandler(myRSSHandler);
                InputSource myInputSource = new InputSource(url.openStream());
                myXMLReader.parse(myInputSource);
                threadnr = 0;
                if (currentUrl.contentEquals(rssUrl)) {
                    myRssFeed = myRSSHandler.getFeed();
                    myInputSource = null;
                    myRSSHandler = null;
                } else {
                    myInputSource = null;
                    myRSSHandler = null;
                    return;
                }
            } catch (Exception e) {
                return;
            }
            runOnUiThread(setRSSAdapter);
        }
