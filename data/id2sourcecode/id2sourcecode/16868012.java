    @Override
    protected void createControlContent(final Page.Request pageRequest, final Channel.ViewResponse viewResponse, final Element controlElement) throws WWWeeePortal.Exception {
        if (pageRequest.isMaximized(getChannel())) {
            createRestoreControlElement(pageRequest, viewResponse, controlElement);
        } else {
            createMaximizeControlElement(pageRequest, viewResponse, controlElement);
        }
        return;
    }
