    public Object[] getPages() {
        String urlString = getPagesIndexUrl();
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return null;
        }
        try {
            String htmlData = new String(FileHelper.readFile(url.openStream()));
            int nextPageIndex = htmlData.indexOf(PAGE_INDEX_PATTERN);
            Hashtable pages = new Hashtable();
            while (nextPageIndex > -1) {
                htmlData = htmlData.substring(nextPageIndex);
                int endIndex = htmlData.indexOf("\">");
                String pageName = htmlData.substring(PAGE_INDEX_PATTERN.length(), endIndex);
                pages.put(pageName, pageName);
                htmlData = htmlData.substring(PAGE_INDEX_PATTERN.length());
                nextPageIndex = htmlData.indexOf(PAGE_INDEX_PATTERN);
            }
            String[] pagesArray = StringArrayHelper.vectorToStringArray(new Vector(pages.keySet()));
            return StringArrayHelper.sort(pagesArray, StringArrayHelper.ASCENDING);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
