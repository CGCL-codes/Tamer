    public void actionSetup() {
        Show oldShow = context.getShow();
        ShowParameters parameters = new ShowParameters();
        parameters.setChannelCount("" + oldShow.getNumberOfChannels());
        parameters.setSubmasterCount("" + oldShow.getNumberOfSubmasters());
        ShowParametersDialog dialog = new ShowParametersDialog(parent, parameters);
        if (dialog.show()) {
            StartParameters startParameters = context.getPreferences().getStartParameters();
            startParameters.setChannelCount(parameters.getChannelCount());
            startParameters.setSubmasterCount(parameters.getSubmasterCount());
            int channelCount = parameters.getIntChannelCount();
            int submasterCount = parameters.getIntSubmasterCount();
            DirtyIndicator dirty = context.getDirtyShow();
            Show newShow = ShowBuilder.build(dirty, channelCount, submasterCount, channelCount, "");
            new ShowCopier(newShow, oldShow).copy();
            context.setShow(newShow);
        }
    }
