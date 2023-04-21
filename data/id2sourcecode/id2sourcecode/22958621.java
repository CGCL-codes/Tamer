    public static RSSFeed parseRSSFeed(final String rssFeedURL, final String rssFeedName) {
        RSSFeed feed = null;
        HttpContext context = new BasicHttpContext();
        HttpGet method = new HttpGet(rssFeedURL);
        try {
            HttpResponse response = ProxyManager.httpClient.execute(method, context);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            document.normalizeDocument();
            if (document != null) {
                Element rss = (Element) document.getElementsByTagName(RSS).item(0);
                Element channel = (Element) rss.getElementsByTagName(CHANNEL).item(0);
                String title = "";
                String link = "";
                String desc = "";
                String lang = "";
                NodeList nodeList = channel.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    if (element.getTagName().equals(TITLE)) {
                        title = element.getTextContent();
                    }
                    if (element.getTagName().equals(LINK)) {
                        link = element.getTextContent();
                    }
                    if (element.getTagName().equals(DESCRIPTION)) {
                        desc = element.getTextContent();
                    }
                    if (element.getTagName().equals(LANGUAGE)) {
                        lang = element.getTextContent();
                    }
                    if (element.getTagName().equals(ITEM)) {
                        break;
                    }
                }
                feed = new RSSFeed();
                feed.feedName = rssFeedName;
                feed.title = title;
                feed.link = link;
                feed.description = desc;
                feed.language = lang;
                List<RSSFeedMessage> feedMessages = new ArrayList<RSSFeedMessage>();
                nodeList = channel.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element item = (Element) nodeList.item(i);
                    if (item.getTagName().equals(ITEM)) {
                        RSSFeedMessage feedMessage = new RSSFeedMessage();
                        NodeList itemNodeList = item.getChildNodes();
                        for (int j = 0; j < itemNodeList.getLength(); j++) {
                            Element element = (Element) itemNodeList.item(j);
                            if (element.getTagName().equals(TITLE)) {
                                feedMessage.title = element.getTextContent();
                            }
                            if (element.getTagName().equals(GUID)) {
                                feedMessage.guid = element.getTextContent();
                            }
                            if (element.getTagName().equals(LINK)) {
                                feedMessage.link = element.getTextContent();
                            }
                            if (element.getTagName().equals(DESCRIPTION)) {
                                feedMessage.description = element.getTextContent();
                            }
                            if (element.getTagName().equals(PUB_DATE)) {
                                feedMessage.pubDate = element.getTextContent();
                            }
                        }
                        feedMessages.add(feedMessage);
                    }
                }
                feed.entries = feedMessages.toArray(new RSSFeedMessage[feedMessages.size()]);
                feedMessages = null;
            }
        } catch (Exception ex) {
            NotifyUtil.error("RSS parse error", "Could not parse " + rssFeedName + " RSS Feed", false);
            feed = null;
        } finally {
            method.abort();
        }
        return feed;
    }
