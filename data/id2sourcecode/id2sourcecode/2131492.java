    public String[] getChannelListByFilterName(ServletContext servletContext, ServletConfig servletConfig, HttpServletRequest request, HttpServletResponse response, HttpSession session, int currentPage, int pageSize, String filterName, String filterItem) {
        List channelList = null;
        String[] contents = new String[5];
        try {
            AjaxChannelListProcessor channelListProcessor = AjaxChannelListProcessor.getAjaxListProcessor();
            AbstractSearchForm form = this.getCurrentForm(filterName, currentPage, pageSize);
            initPageContext(servletContext, servletConfig, request, response, session, filterName, filterItem);
            channelList = channelListProcessor.getChannelList(threadContext, form);
            String[] content = AjaxChannelFormatterAdapter.getAjaxChannelFormatter(threadContext.getPageContext(), pageSize, channelList, filterName).getContent();
            contents[0] = filterName;
            contents[1] = content[0];
            contents[2] = channelList.size() + "";
            contents[3] = content[1];
            contents[4] = content[2];
        } catch (Exception ex) {
            log.error(ex, ex);
        } finally {
            try {
                threadContext.closeConnection();
                channelList = null;
            } catch (Exception e) {
                log.error(e, e);
            }
        }
        return contents;
    }
