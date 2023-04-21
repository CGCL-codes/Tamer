    private AbstractSearchForm getCurrentForm(String filterName, int currentPage, int pageSize) {
        AbstractSearchForm form = AjaxFormAdapter.getChannelAdapterForm(filterName);
        form.setCurStartRowNo("" + currentPage);
        form.setPageOffset("" + pageSize);
        return form;
    }
