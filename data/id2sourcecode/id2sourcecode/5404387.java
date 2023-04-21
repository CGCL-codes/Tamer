    public void addResourcesFromPage(final ZipOutStreamTaskAdapter out, final String pageURL, final String zipPath) throws IOException, TaskTimeoutException {
        out.putNextEntry(new ZipEntry(zipPath));
        out.closeEntry();
        List<PageEntity> children = getDao().getPageDao().getByParent(pageURL);
        for (PageEntity child : children) {
            addResourcesFromPage(out, child.getFriendlyURL(), zipPath + child.getPageFriendlyURL() + "/");
        }
        List<PageEntity> pages = getDao().getPageDao().selectByUrl(pageURL);
        if (pages.size() > 0) {
            addPageFiles(out, pages.get(0), zipPath);
        }
    }
