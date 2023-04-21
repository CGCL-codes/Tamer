    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        this.setTheme(this.getVisitedPortal(request));
        User user = this.getUser(request);
        Locale currentLocale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        if (currentLocale == null) {
            String locale = _DEFAULT_LOCALE;
            if (user != null) locale = user.getLanguage(); else {
                Cookie[] theCookies = ((HttpServletRequest) pageContext.getRequest()).getCookies();
                if (theCookies != null) {
                    for (int i = 0; i < theCookies.length; i++) {
                        Cookie aCookie = theCookies[i];
                        if (aCookie.getName().equals(_REMEMBER_LOCALE)) {
                            if (aCookie.getValue() != null) locale = aCookie.getValue();
                            break;
                        }
                    }
                }
            }
            String[] localeParams = locale.split("_");
            if (localeParams.length > 1) currentLocale = new Locale(localeParams[0], localeParams[1]); else currentLocale = new Locale(localeParams[0]);
            Config.set(request.getSession(), Config.FMT_LOCALE, currentLocale);
        }
        request.getSession().setAttribute(_CURRENT_LOCALE, currentLocale.toString());
        try {
            List<LabelBean> list = new LinkedList<LabelBean>();
            list.add(new LabelBean("portalJSON", this.runCommand(request, response, "visitPortal").toString()));
            request.setAttribute("channel", this.getChannel());
            list.add(new LabelBean("tabsJSON", this.runCommand(request, response, "getPortalTabsByChannel").toString()));
            List<PortalTab> tabs = getPortalService().getPortalTabByOwner(this.getChannel(), OrganizationThreadLocal.getOrg());
            PortalTab defaultTab = null;
            while (tabs != null && tabs.size() > 0) {
                defaultTab = tabs.get(0);
                tabs = this.getPortalService().getPortalTabByParent(defaultTab.getId());
                list.add(new LabelBean("tab" + defaultTab.getId() + "TabsJSON", JSONUtil.getPortalTabData(request, tabs, true, isAdmin(request), user, 0).toString()));
            }
            request.setAttribute("tabId", String.valueOf(defaultTab.getId()));
            list.add(new LabelBean("tab" + defaultTab.getId() + "PortletsJSON", this.runCommand(request, response, "getPortletsByVisitTab").toString()));
            request.setAttribute(_PORTAL_INIT_LIST, list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return SKIP_BODY;
    }
