    public void crawl(CrawlURI crawlURI) {
        try {
            fetch.process(crawlURI);
            overviewPageProcessor.process(crawlURI);
            if (crawlURI.getCrawlStatus() == CrawlStatusCodes.PAGE_PROCESS_SUCCESS) {
                BaikeDataSource baike = (BaikeDataSource) crawlURI.getModel();
                baike.setOriHtmlUrl(crawlURI.getCrawlUrl());
                String area = baike.getArea();
                if (StringUtils.isNotEmpty(area)) {
                    area = area.replaceAll("\\s*/\\s*", ",");
                }
                String language = baike.getLanguage();
                if (StringUtils.isNotEmpty(language)) {
                    language = language.replaceAll("\\s*/\\s*", ",");
                }
                String channelName = baike.getChannelName();
                String tags = baike.getTags();
                if ("电影".equalsIgnoreCase(channelName)) {
                    baike.setArea(area);
                    baike.setLanguage(language);
                    if (tags != null && tags.indexOf("动漫") >= 0) {
                        baike.setChannelCode(Constants.CHANNEL_CODE_ANIME);
                    } else if (tags != null && tags.indexOf("电视剧") >= 0) {
                        baike.setChannelCode(Constants.CHANNEL_CODE_TELEPLAY);
                    }
                    dbWriter.process(crawlURI);
                } else if ("专辑".equalsIgnoreCase(channelName)) {
                    baike.setChannelName("音乐");
                    baike.setChannelCode(Constants.CHANNEL_CODE_MUSIC);
                    dbWriter.process(crawlURI);
                }
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
