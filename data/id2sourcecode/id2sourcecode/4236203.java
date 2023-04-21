    public static AbstractSearchForm getChannelAdapterForm(String filterName) {
        if (AjaxConstant.TASKINBOXFILTER.equals(filterName)) {
            return new ListWorkflowProgressForm();
        } else {
            return new ListPersonalHomeForm();
        }
    }
