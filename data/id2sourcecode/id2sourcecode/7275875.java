    public List<Blog> searchRecentUpdatedBlogs() throws URISyntaxException, IOException {
        Date date = DateUtils.addDays(new Date(), DAYS_BEFORE);
        String dateStr = DateFormatUtils.format(date, "yyyy/MM/dd");
        String urlString = "http://cloud.istudy.ne.jp/ies/blogView.do?";
        urlString = urlString + "TYPE=1610&SUB_TYPE=1611&OPERATION=0&USER_OPERATION=2&PAGE_COUNT=0";
        urlString = urlString + "&START_DATE=" + dateStr;
        URL url = new URL(urlString);
        InputStream in = url.openStream();
        String textHtml = getTextFromInputStream(in);
        Pattern pattern = Pattern.compile("<a href=\"javascript:next\\(0,'(\\d+)', '(\\w+)'\\)\">(.*)</a>");
        Matcher matcher = pattern.matcher(textHtml);
        List<Blog> list = new ArrayList<Blog>();
        while (matcher.find()) {
            Blog blog = new Blog();
            blog.setBlogId(matcher.group(1));
            blog.setCustCd(matcher.group(2));
            blog.setBlogName(matcher.group(3));
            list.add(blog);
        }
        return list;
    }
