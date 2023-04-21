    public void sendTrackback(String entryId, List<String> trackbackURLs) {
        Entry entry = getEntry(entryId);
        if (entry == null) {
            return;
        }
        String title = entry.getTitle();
        String excerpt = StringUtils.left(MyblogUtil.removeHTML(entry.getContent()), 255);
        String url = getSiteConfig().getSiteURL() + "/entry/";
        if (StringUtils.isNotBlank(entry.getName())) {
            url += entry.getName() + ".html";
        } else {
            url += "id/" + entry.getId() + ".html";
        }
        String blogName = getSiteConfig().getSiteName();
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(URLEncoder.encode("title", "UTF-8")).append("=").append(URLEncoder.encode(title, "UTF-8"));
            sb.append("&").append(URLEncoder.encode("excerpt", "UTF-8")).append("=").append(URLEncoder.encode(excerpt, "UTF-8"));
            sb.append("&").append(URLEncoder.encode("url", "UTF-8")).append("=").append(URLEncoder.encode(url, "UTF-8"));
            sb.append("&").append(URLEncoder.encode("blog_name", "UTF-8")).append("=").append(URLEncoder.encode(blogName, "UTF-8"));
            for (String trackURL : trackbackURLs) {
                URL tburl = new URL(trackURL);
                HttpURLConnection conn = (HttpURLConnection) tburl.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                BufferedReader rd = null;
                try {
                    wr.write(sb.toString());
                    wr.flush();
                    boolean inputAvailable = false;
                    try {
                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        inputAvailable = true;
                    } catch (Throwable e) {
                        log.error("connection error", e);
                    }
                    if (inputAvailable) {
                        String line;
                        StringBuffer resultBuff = new StringBuffer();
                        while ((line = rd.readLine()) != null) {
                            resultBuff.append(TextUtil.escapeHTML(line, true));
                            resultBuff.append("<br />");
                        }
                        log.info("trackback ok:" + resultBuff);
                    }
                    if (conn.getResponseCode() > 399) {
                        log.info("trackback error with errorCode:" + conn.getResponseCode());
                    } else {
                        log.debug("trackback ok with code:" + conn.getResponseCode());
                    }
                } finally {
                    if (wr != null) wr.close();
                    if (rd != null) rd.close();
                }
            }
        } catch (IOException ex) {
            log.error("trackback error:", ex);
        }
    }
