    protected void innerProcess(CrawlURI crawlUri) {
        if (!canProcess(crawlUri)) {
            return;
        }
        Baike baike = (Baike) crawlUri.getModel();
        if (baike == null) {
            baike = new Baike();
        }
        try {
            String sourceSite = crawlUri.getSourceSite();
            String crawlUrl = crawlUri.getCrawlUrl();
            String crawlResult = crawlUri.getCrawlResult();
            String domainReg = "http://(.*?)/";
            String domain = PageProcessorUtils.getValue(domainReg, crawlUrl);
            String movieText = crawlResult.replaceAll("&nbsp[;]?", "");
            baike.setSourceSite(sourceSite);
            Set<Object> keySet = processPageRegex.keySet();
            for (Object key : keySet) {
                String name = (String) key;
                if (processPageRegex.containsKey(name)) {
                    String value = PageProcessorUtils.getValue(processPageRegex.getProperty(name), movieText);
                    if (StringUtils.isNotEmpty(value) && PropertyUtils.isWriteable(baike, name)) {
                        if (name.equalsIgnoreCase("summary")) {
                            if (PageProcessorUtils.isMatch(P_REG, value)) {
                                value = value.replaceAll("<\\s*/p\\s*>\\s*?<\\s*p[^<]*?>", "::P_FLAG");
                                value = value.replaceAll("<BR>", "::P_FLAG");
                                value = value.replaceAll("<[^<]*?>", "");
                                value = value.replaceAll("::P_FLAG", "<br/>");
                            } else {
                                value = value.replaceAll("<BR>", "::P_FLAG");
                                value = value.replaceAll("<[^<]*?>", "");
                                value = value.replaceAll("::P_FLAG", "<br/>");
                            }
                        } else {
                            if (PageProcessorUtils.isMatch(LI_REG, value)) {
                                value = PageProcessorUtils.getValues(LI_REG, value);
                            }
                            if (PageProcessorUtils.isMatch(A_REG, value)) {
                                value = PageProcessorUtils.getValues(A_REG, value);
                            }
                            value = value.replaceAll("<[^<]*?>", "");
                        }
                        value = StringUtils.trim(value);
                        PropertyUtils.setProperty(baike, name, value);
                    }
                }
            }
            String name = baike.getName();
            if (StringUtils.isEmpty(name)) {
                baike.setName(baike.getEnName());
            }
            if (StringUtils.isEmpty(name) || StringUtils.isEmpty(baike.getOriId())) {
                crawlUri.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_INVALID);
                return;
            }
            baike.setFirstLetter(PageProcessorUtils.getFirstLetter(name));
            String oriHtmlUrl = baike.getOriHtmlUrl();
            if (StringUtils.isNotEmpty(oriHtmlUrl) && !oriHtmlUrl.startsWith("http://")) {
                oriHtmlUrl = "http://" + domain + oriHtmlUrl;
                baike.setOriHtmlUrl(oriHtmlUrl);
            } else if (processPageRegex.containsKey("oriHtmlUrl")) {
                String rule = (String) processPageRegex.getProperty("oriHtmlUrl");
                String result = PageProcessorUtils.replaceFlag(rule, "::ORI_ID", baike.getOriId());
                if (!result.equals(rule)) {
                    baike.setOriHtmlUrl(oriHtmlUrl);
                }
            }
            String channelName = baike.getChannelName();
            if (channelName != null && channelName.indexOf("电视") >= 0 && !channelName.equals("电视电影")) {
                baike.setChannelCode("teleplay");
            } else {
                baike.setChannelCode("movie");
            }
            String channelCode = baike.getChannelCode();
            if ("tv".equalsIgnoreCase(channelCode)) {
                channelCode = "varietyshow";
            }
            String synopsis = baike.getSynopsis();
            if (StringUtils.isEmpty(synopsis) && processPageRegex.containsKey("synopsis2")) {
                String reg = (String) processPageRegex.getProperty("synopsis2");
                String value = PageProcessorUtils.getValue(reg, movieText);
                baike.setSynopsis(value);
            }
            if (processPageRegex.containsKey("releaseDateYear")) {
                String releaseDateYear = PageProcessorUtils.getValue(processPageRegex.getProperty("releaseDateYear"), movieText);
                String releaseDateMonth = PageProcessorUtils.getValue(processPageRegex.getProperty("releaseDateMonth"), movieText);
                String releaseDateDay = PageProcessorUtils.getValue(processPageRegex.getProperty("releaseDateDay"), movieText);
                StringBuffer releaseDatesb = new StringBuffer();
                if (StringUtils.isNotEmpty(releaseDateYear)) {
                    releaseDatesb.append(releaseDateYear);
                    releaseDatesb.append("年");
                    if (releaseDateMonth.length() == 1) {
                        releaseDatesb.append("0");
                    }
                    releaseDatesb.append(releaseDateMonth);
                    releaseDatesb.append("月");
                    if (releaseDateDay.length() == 1) {
                        releaseDatesb.append("0");
                    }
                    releaseDatesb.append(releaseDateDay);
                    releaseDatesb.append("日");
                }
                String releaseDate = releaseDatesb.toString();
                baike.setReleaseDate(releaseDate);
                baike.setCreateUser("uusee-crawler");
                baike.setUpdateUser("uusee-crawler");
                baike.setCreateDate(new Date());
                baike.setUpdateDate(new Date());
            }
            crawlUri.setModel(baike);
            crawlUri.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.equals(e);
            crawlUri.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_EXCEPTION);
        }
    }
