    protected void innerProcess(CrawlURI crawlUri) {
        if (!canProcess(crawlUri)) {
            return;
        }
        try {
            String sourceSite = crawlUri.getSourceSite();
            String crawlUrl = crawlUri.getCrawlUrl();
            String crawlResult = crawlUri.getCrawlResult();
            String domainReg = "http://(.*?)/";
            String domain = PageProcessorUtils.getValue(domainReg, crawlUrl);
            String modelText = crawlResult.replaceAll("&nbsp[;]?", "");
            BaikeDataSource baike = new BaikeDataSource();
            Set<Object> keySet = processPageRegex.keySet();
            for (Object key : keySet) {
                String name = (String) key;
                if (processPageRegex.containsKey(name)) {
                    String value = PageProcessorUtils.getValue(processPageRegex.getProperty(name), modelText);
                    if (StringUtils.isNotEmpty(value) && PropertyUtils.isWriteable(baike, name)) {
                        if (name.equalsIgnoreCase("summary") || name.equalsIgnoreCase("synopsis")) {
                            if (PageProcessorUtils.isMatch(P_REG, value)) {
                                value = value.replaceAll("<\\s*/p\\s*>\\s*?<\\s*p[^<]*?>", "::P_FLAG");
                                value = value.replaceAll("<BR\\s*[/]{0,1}>", "::P_FLAG");
                                value = value.replaceAll("<br\\s*[/]{0,1}>", "::P_FLAG");
                                value = value.replaceAll("<[^<]*?>", "");
                                value = value.replaceAll("::P_FLAG", "<br/>");
                            } else {
                                value = value.replaceAll("<BR[\\s]*[/]{0,1}>", "::P_FLAG");
                                value = value.replaceAll("<br[\\s]*[/]{0,1}>", "::P_FLAG");
                                value = value.replaceAll("<[^<]*?>", "");
                                value = value.replaceAll("::P_FLAG", "<br/>");
                                value = value.replaceAll("<br/><br/>代码.*", "");
                                value = value.replaceAll("<br/><br/>分享.*", "");
                            }
                        } else if (name.equalsIgnoreCase("itemList")) {
                            value = value.replaceAll("<BR[\\s]*[/]{0,1}>", "::P_FLAG");
                            value = value.replaceAll("<br[\\s]*[/]{0,1}>", "::P_FLAG");
                            value = value.replaceAll("<[^<]*?>", "");
                            value = value.replaceAll("::P_FLAG", "<br/>");
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
            String oriId = baike.getOriId();
            String name = baike.getName();
            if (StringUtils.isEmpty(name)) {
                baike.setName(baike.getEnName());
            }
            if (StringUtils.isEmpty(name) || StringUtils.isEmpty(oriId)) {
                crawlUri.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_INVALID);
                crawlUri.setModel(null);
                return;
            }
            baike.setSourceSite(sourceSite);
            String oriHtmlUrl = baike.getOriHtmlUrl();
            if (StringUtils.isNotEmpty(oriHtmlUrl) && !oriHtmlUrl.startsWith("http://")) {
                oriHtmlUrl = "http://" + domain + oriHtmlUrl;
                baike.setOriHtmlUrl(oriHtmlUrl);
            } else if (processPageRegex.containsKey("oriHtmlUrlRule")) {
                String rule = (String) processPageRegex.getProperty("oriHtmlUrlRule");
                String result = PageProcessorUtils.replaceFlag(rule, "::ORI_ID", oriId);
                if (!result.equals(rule)) {
                    baike.setOriHtmlUrl(result);
                }
            }
            String oriLogoUrl = baike.getOriLogoUrl();
            if (StringUtils.isNotEmpty(oriLogoUrl) && !oriLogoUrl.startsWith("http://")) {
                oriLogoUrl = "http://" + domain + oriLogoUrl;
                baike.setOriLogoUrl(oriLogoUrl);
            }
            if (sourceSite.equalsIgnoreCase("www.mtime.com")) {
                String channelName = baike.getChannelName();
                if (channelName != null && channelName.indexOf("电视") >= 0 && !channelName.equals("电视电影")) {
                    baike.setChannelCode("teleplay");
                    baike.setChannelName("电视剧");
                } else {
                    baike.setChannelCode("movie");
                    baike.setChannelName("电影");
                }
            }
            if (sourceSite.equalsIgnoreCase("www.douban.com")) {
                String synopsis = baike.getSynopsis();
                if (StringUtils.isEmpty(synopsis) && processPageRegex.containsKey("synopsis2")) {
                    String reg = (String) processPageRegex.getProperty("synopsis2");
                    String value = PageProcessorUtils.getValue(reg, modelText);
                    if (PageProcessorUtils.isMatch(P_REG, value)) {
                        value = value.replaceAll("<\\s*/p\\s*>\\s*?<\\s*p[^<]*?>", "::P_FLAG");
                        value = value.replaceAll("<BR>", "::P_FLAG");
                        value = value.replaceAll("<br/>", "::P_FLAG");
                        value = value.replaceAll("<[^<]*?>", "");
                        value = value.replaceAll("::P_FLAG", "<br/>");
                    } else {
                        value = value.replaceAll("<BR>", "::P_FLAG");
                        value = value.replaceAll("<br/>", "::P_FLAG");
                        value = value.replaceAll("<[^<]*?>", "");
                        value = value.replaceAll("::P_FLAG", "<br/>");
                    }
                    baike.setSynopsis(value);
                }
            }
            if (sourceSite.equalsIgnoreCase("www.xunlei.com")) {
                String channelCode = baike.getChannelCode();
                if ("tv".equalsIgnoreCase(channelCode)) {
                    channelCode = "varietyshow";
                }
            }
            if (sourceSite.equalsIgnoreCase("www.verycd.com")) {
                String channelCode = baike.getChannelCode();
                if ("cartoon".equalsIgnoreCase(channelCode)) {
                    baike.setChannelCode(Constants.CHANNEL_CODE_ANIME);
                } else if ("tv".equalsIgnoreCase(channelCode)) {
                    baike.setChannelCode(Constants.CHANNEL_CODE_VARIETYSHOW);
                } else if ("movie".equalsIgnoreCase(channelCode)) {
                    baike.setChannelCode(Constants.CHANNEL_CODE_MOVIE);
                } else if ("teleplay".equalsIgnoreCase(channelCode)) {
                    baike.setChannelCode(Constants.CHANNEL_CODE_TELEPLAY);
                } else if ("music".equalsIgnoreCase(channelCode)) {
                    baike.setChannelCode(Constants.CHANNEL_CODE_MUSIC);
                    String reg = (String) processPageRegex.getProperty("musicgenre");
                    String value = PageProcessorUtils.getValue(reg, modelText);
                    baike.setGenre(value);
                } else {
                    crawlUri.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_INVALID);
                    crawlUri.setModel(null);
                    return;
                }
            }
            if (sourceSite.equalsIgnoreCase("www.imdb.cn")) {
                String channelCode = baike.getChannelCode();
                if ("TV-Series".equalsIgnoreCase(channelCode)) {
                    baike.setChannelCode(Constants.CHANNEL_CODE_TELEPLAY);
                } else {
                    baike.setChannelCode(Constants.CHANNEL_CODE_MOVIE);
                }
                String reg = (String) processPageRegex.getProperty("synopsis");
                String value = PageProcessorUtils.getValue(reg, modelText);
                baike.setSynopsis(value);
            }
            if (processPageRegex.containsKey("releaseDateYear")) {
                String releaseDateYear = PageProcessorUtils.getValue(processPageRegex.getProperty("releaseDateYear"), modelText);
                String releaseDateMonth = PageProcessorUtils.getValue(processPageRegex.getProperty("releaseDateMonth"), modelText);
                String releaseDateDay = PageProcessorUtils.getValue(processPageRegex.getProperty("releaseDateDay"), modelText);
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
            }
            baike.setCreateDate(new Date());
            baike.setUpdateDate(new Date());
            crawlUri.setModel(baike);
            crawlUri.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.equals(e);
            crawlUri.setCrawlStatus(CrawlStatusCodes.PAGE_PROCESS_EXCEPTION);
        }
    }
