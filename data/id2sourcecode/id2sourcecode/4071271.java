    public void renderPage(String page, Hashtable<String, String> parameters, HtmlWriter writer) {
        if ("threads".equals(page)) {
            renderThreadsPage(parameters, writer);
        } else if ("process_threads".equals(page)) {
            renderProcessThreadsPage(parameters, writer);
        } else if ("filter_threads".equals(page)) {
            renderFilterThreadsPage(parameters, writer);
        } else if ("options".equals(page)) {
            renderOptionsPage(parameters, writer);
        } else {
            renderMainPage(parameters, writer);
        }
    }
