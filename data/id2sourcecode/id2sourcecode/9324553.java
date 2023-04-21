    public boolean doFilter(ProxyHandler handler) {
        logger.debug("#doFilter cid:" + handler.getChannelId());
        User user = (User) handler.getRequestAttribute(ServerBaseHandler.ATTRIBUTE_USER);
        List<String> roles = user.getRolesList();
        if (roles == null) {
            return false;
        }
        if (roles.contains("admin")) {
            return true;
        }
        HeaderParser requestHeader = handler.getRequestHeader();
        String filterCookie = requestHeader.getAndRemoveCookieHeader(filterCookieName);
        if (filterCookie != null && filterCookie.startsWith("true/")) {
            return true;
        }
        if (filterCookie != null && filterCookie.startsWith("false/")) {
            handler.completeResponse("200", "phantom proxy filter blocked");
            return false;
        }
        List<Long> whiteList = null;
        List<Long> blackList = null;
        for (String role : roles) {
            List<Long> bl = categoryIdList(role, true);
            if (bl.size() != 0) {
                if (blackList == null) {
                    blackList = new ArrayList<Long>();
                }
                blackList.addAll(bl);
            }
            List<Long> wl = categoryIdList(role, false);
            if (wl.size() != 0) {
                if (whiteList == null) {
                    whiteList = new ArrayList<Long>();
                }
                whiteList.addAll(wl);
            }
        }
        MappingResult mapping = handler.getRequestMapping();
        ServerParser server = mapping.getResolveServer();
        String path = mapping.getResolvePath();
        long start = System.currentTimeMillis();
        Collection<FilterEntry> list = matchFilter(server.getHost());
        logger.debug("FilterEntry.matchFilter.time:" + (System.currentTimeMillis() - start));
        FilterCategory category = matchCategory(whiteList, list, path);
        if (category != null) {
            if (!category.isUrl()) {
                path = "/";
            }
            String okCookie = Cookie.formatSetCookieHeader(filterCookieName, "true/" + System.currentTimeMillis(), null, path);
            handler.addResponseHeader(HeaderParser.SET_COOKIE_HEADER, okCookie);
            return true;
        }
        category = matchCategory(blackList, list, path);
        if (category != null) {
            if (!category.isUrl()) {
                path = "/";
            }
            String ngCookie = Cookie.formatSetCookieHeader(filterCookieName, "false/" + System.currentTimeMillis(), null, path);
            handler.setHeader(HeaderParser.SET_COOKIE_HEADER, ngCookie);
            handler.completeResponse("200", "phantom proxy filter blocked");
            return false;
        }
        String okCookie = Cookie.formatSetCookieHeader(filterCookieName, "true/" + System.currentTimeMillis(), null, "/");
        handler.addResponseHeader(HeaderParser.SET_COOKIE_HEADER, okCookie);
        return true;
    }
