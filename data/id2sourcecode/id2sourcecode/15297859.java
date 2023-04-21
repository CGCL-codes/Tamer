    public int fetch(URLEntity urlEntity, PageEntity page) {
        String toFetchURL = urlEntity.getUrl();
        toFetchURL.replaceAll(" ", "%20");
        HttpGet get = new HttpGet(toFetchURL);
        HttpEntity entity = null;
        try {
            HttpResponse response = httpclient.execute(get);
            entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND) {
                    logger.info("Failed: " + response.getStatusLine().toString() + ", while fetching " + toFetchURL);
                }
                return response.getStatusLine().getStatusCode();
            }
            String url = get.getURI().toString();
            if (entity != null) {
                long size = getContentSize(response);
                if (size > MAX_DOWNLOAD_SIZE) {
                    entity.consumeContent();
                    return Fetcher.PageTooBig;
                }
                String encode = getContentEncode(response);
                String type = getContentType(urlEntity, response);
                if (type.indexOf(';') >= 0) {
                    String[] str = type.split(";");
                    type = str[0];
                    if (encode == null && str.length > 1) {
                        encode = str[1].trim().replaceAll("charset=", "");
                    }
                }
                final byte[] content = downloadContent(response);
                final String md5 = Md5.getDigest("mycrawler", content);
                final String format = mimeFormater.JudgeFormat(url, type);
                if (content != null) {
                    size = content.length;
                    page.setUrl(url);
                    page.setContent(content);
                    page.setSize(size);
                    page.setEncode(encode);
                    page.setType(type);
                    page.setDownloadDate(new Date());
                    page.setAnchorText(urlEntity.getAnchor_text());
                    page.setDigest(md5);
                    page.setFormat(format);
                    if (urlEntity.getTitle() != null) {
                        page.setTitle(urlEntity.getTitle());
                    }
                    if (urlEntity.getDiscription() != null) {
                        page.setDiscription(urlEntity.getDiscription());
                    }
                    if (urlEntity.getPublishData() != null) {
                        page.setPublishData(urlEntity.getPublishData());
                    }
                    return Fetcher.OK;
                } else {
                    return Fetcher.PageLoadError;
                }
            } else {
                get.abort();
            }
        } catch (Exception e) {
            logger.error(e.getMessage() + " while fetching " + toFetchURL);
        } finally {
            try {
                if (entity != null) {
                    entity.consumeContent();
                }
                if (get != null) {
                    get.abort();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return UnknownError;
    }
