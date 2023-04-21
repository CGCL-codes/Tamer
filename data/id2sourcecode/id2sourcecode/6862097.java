    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (_log.isDebugEnabled()) {
            if (USE_FILTER) {
                _log.debug("Velocity is enabled");
            } else {
                _log.debug("Velocity is disabled");
            }
        }
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;
        String completeURL = Http.getCompleteURL(httpReq);
        if (USE_FILTER && isMatchingURL(completeURL)) {
            if (_log.isDebugEnabled()) {
                _log.debug("Processing " + completeURL);
            }
            CacheResponse cacheResponse = new CacheResponse(httpRes, ENCODING);
            chain.doFilter(req, cacheResponse);
            VelocityContext context = new VelocityContext();
            StringReader reader = new StringReader(new String(cacheResponse.getData()));
            StringWriter writer = new StringWriter();
            ThemeDisplay themeDisplay = null;
            try {
                long companyId = ParamUtil.getLong(req, "companyId");
                Company company = CompanyLocalServiceUtil.getCompanyById(companyId);
                String contextPath = PortalUtil.getPathContext();
                String languageId = ParamUtil.getString(req, "languageId");
                Locale locale = LocaleUtil.fromLanguageId(languageId);
                String themeId = ParamUtil.getString(req, "themeId");
                String colorSchemeId = ParamUtil.getString(req, "colorSchemeId");
                boolean wapTheme = BrowserSniffer.is_wap_xhtml(httpReq);
                Theme theme = ThemeLocalUtil.getTheme(companyId, themeId, wapTheme);
                ColorScheme colorScheme = ThemeLocalUtil.getColorScheme(companyId, theme.getThemeId(), colorSchemeId, wapTheme);
                themeDisplay = ThemeDisplayFactory.create();
                themeDisplay.setCompany(company);
                themeDisplay.setLocale(locale);
                themeDisplay.setLookAndFeel(contextPath, theme, colorScheme);
                themeDisplay.setPathContext(contextPath);
                req.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
                VelocityVariables.insertVariables(context, httpReq);
                Velocity.evaluate(context, writer, VelocityFilter.class.getName(), reader);
            } catch (Exception e) {
                _log.error(e, e);
            } finally {
                try {
                    if (themeDisplay != null) {
                        ThemeDisplayFactory.recycle(themeDisplay);
                    }
                } catch (Exception e) {
                }
            }
            CacheResponseData data = new CacheResponseData(writer.toString().getBytes(ENCODING), cacheResponse.getContentType(), cacheResponse.getHeaders());
            CacheResponseUtil.write(httpRes, data);
        } else {
            if (_log.isDebugEnabled()) {
                _log.debug("Not processing " + completeURL);
            }
            chain.doFilter(req, res);
        }
    }
