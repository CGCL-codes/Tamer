    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (path == ITEM) {
            Uri itemUri = Uri.withAppendedPath(feed, RssFeed.RssItem.CONTENT_DIRECTORY);
            if (oldUpdate.before(pubDate)) {
                Log.v(TAG, "new item...");
                values.clear();
                values.put(RssItem.TITLE, titreArticle);
                values.put(RssItem.FEED_ID, feed.getLastPathSegment());
                values.put(RssItem.DESCRIPTION, description);
                values.put(RssItem.URL, linkArticle.toString());
                values.put(RssItem.MODIFIED_DATE, pubDate.getTime());
                resolver.insert(itemUri, values);
            }
            if (newest.before(pubDate)) newest = pubDate;
            titreArticle = null;
            description = null;
            linkArticle = null;
        } else if (path == ITEM_TITLE) {
            titreArticle = current.toString();
        } else if (path == ITEM_DESCRIPTION) {
            description = current.toString();
        } else if (path == ITEM_LINK) {
            try {
                Log.v(TAG, current.toString());
                linkArticle = new URL(current.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (path == ITEM_PUBDATE) {
            pubDate = DateParser.parseDate(current.toString());
        } else if (path == FEED_TITLE) {
            feedTitle = current.toString();
        } else if (path == FEED_ICON) {
            try {
                URI imgUrl = URI.create(current.toString());
                HttpClient client = new DefaultHttpClient();
                HttpGet req = new HttpGet(imgUrl);
                HttpResponse r = client.execute(req);
                img = new byte[0];
                byte[] buf = new byte[1024];
                int read;
                InputStream is = r.getEntity().getContent();
                while ((read = is.read(buf)) != -1) {
                    byte[] newImg = new byte[img.length + read];
                    System.arraycopy(img, 0, newImg, 0, img.length);
                    System.arraycopy(buf, 0, newImg, img.length, read);
                    img = newImg;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            path ^= Elements.valueOf(localName).value;
        } catch (IllegalArgumentException e) {
            if (--nbOther == 0) path ^= Elements._other.value;
        }
    }
