    public List getChannelList(AjaxThreadContext threadContext, AbstractSearchForm form) throws Exception {
        String filterName = threadContext.getRequestAttribute(AjaxConstant.FILTERNAME).toString();
        if (AjaxConstant.MEETINGFILTER.equalsIgnoreCase(filterName)) {
            return getMeetingChannelList(threadContext, form);
        }
        if (AjaxConstant.MESSAGEINBOXFILTER.equalsIgnoreCase(filterName)) {
            return getMessageChannelList(threadContext, form);
        }
        if (AjaxConstant.PROGRESSMONITORFILTER.equalsIgnoreCase(filterName)) {
            return getProgressMonitorChannelList(threadContext, form);
        }
        if (AjaxConstant.RECENTDOCFILTER.equalsIgnoreCase(filterName)) {
            return getRecentDocChannelList(threadContext, form);
        }
        if (AjaxConstant.TASKINBOXFILTER.equalsIgnoreCase(filterName)) {
            return getTaskInboxChannelList(threadContext, form);
        }
        return Collections.EMPTY_LIST;
    }
