    NewsItem[] getUnreadItem() {
        List<NewsItem> items = new ArrayList<NewsItem>();
        RssDescriptor[] descs = RssFactory.getInstance().getRssItems();
        for (RssDescriptor rssDescriptor : descs) {
            Rssloader loader = new Rssloader(rssDescriptor.getUrl());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date last = null;
                try {
                    last = formatter.parse(prop.getProperty(rssDescriptor.getUrl(), "0"));
                } catch (ParseException e) {
                    String initialDate = Platform.getPreferencesService().getString(Activator.ID, "initialDate", formatter.format(new Date()), new IScopeContext[] { new DefaultScope() });
                    last = formatter.parse(initialDate);
                }
                if (last == null) throw new IllegalArgumentException("Invalid initial date.");
                prop.setProperty(rssDescriptor.getUrl(), formatter.format(last.getTime()));
                loader.load();
                NewsItem[] newsItems = loader.getItems();
                Date unread = null;
                for (int i = 0; i < newsItems.length; i++) {
                    if (isUnread(last, newsItems[i])) {
                        items.add(newsItems[i]);
                        if (unread == null) unread = newsItems[i].getPubDateParsed(); else if (unread.compareTo(newsItems[i].getPubDateParsed()) < 0) unread = newsItems[i].getPubDateParsed();
                    } else break;
                }
                if (unread != null) prop.setProperty(rssDescriptor.getUrl(), formatter.format(unread));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (NewsfeedFactoryException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return items.toArray(new NewsItem[items.size()]);
    }
