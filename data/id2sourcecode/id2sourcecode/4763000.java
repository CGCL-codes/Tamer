    public ChannelIF handle(String content) throws ContentHandlerException {
        log.trace("Handling content using handler ... " + this.getTitle());
        Matcher m = itemPattern.matcher(content);
        while (m.find()) {
            String title = null;
            String description = null;
            String link = null;
            String date = null;
            try {
                Integer titleOrder = itemPatternOrder.get("title");
                Integer descOrder = itemPatternOrder.get("description");
                Integer linkOrder = itemPatternOrder.get("link");
                Integer dateOrder = itemPatternOrder.get("date");
                log.info("pattern order ... title=" + titleOrder + ", desc=" + descOrder + ", link=" + linkOrder + ", date=" + dateOrder);
                title = (titleOrder == null) ? "Untitled" : m.group(itemPatternOrder.get("title"));
                description = (descOrder == null) ? "" : m.group(itemPatternOrder.get("description"));
                link = (linkOrder == null) ? "http://unknown" : m.group(itemPatternOrder.get("link"));
                date = (dateOrder == null) ? "" : m.group(itemPatternOrder.get("date"));
                URL linkUrl = URLUtil.getAbsoluteLink(this.getSite(), link);
                log.debug(title + "," + description + "," + linkUrl);
                ItemIF item = addItem(title, description, linkUrl);
                try {
                    Date feedDate = dateFormatter.parse(date);
                    item.setDate(feedDate);
                } catch (ParseException e) {
                    log.warn("Date malformatted: " + e.getMessage() + "; Src:" + date);
                }
            } catch (MalformedURLException e) {
                try {
                    log.warn("URL malformatted: " + e.getMessage());
                    this.addItem(title, description, new URL("http://unknown.com/"));
                } catch (Exception error) {
                    log.warn("URL malformatted! Cannot recovered the link, ignored.");
                }
            }
        }
        return this.getChannel();
    }
