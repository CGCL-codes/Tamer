    public ViewAiringItemDetails(GWTMediaFile gfile, BrowsePanel controller) {
        initWidget(uiBinder.createAndBindUi(this));
        History.newItem("viewairing", false);
        this.file = gfile;
        this.controller = controller;
        poster.addErrorHandler(new ErrorHandler() {

            @Override
            public void onError(ErrorEvent event) {
                GWT.log("Invalid URL: " + poster.getUrl());
                poster.setUrl("images/128x128/video2.png");
            }
        });
        poster.setUrl("fanart?mediafile=" + gfile.getAiringId() + "&scalex=200&artifact=poster");
        showTitle.setText(file.getTitle());
        if (file.getMinorTitle() != null && !file.getMinorTitle().equals(file.getTitle())) {
            episodeName.setText("\"" + file.getMinorTitle() + "\"");
        }
        description.setText("");
        controller.getServices().loadMetadata(file, new AsyncCallback<GWTMediaMetadata>() {

            @Override
            public void onSuccess(GWTMediaMetadata result) {
                updateDisplay(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                Application.fireErrorEvent("Unable to load metadata for " + file.getTitle(), caught);
            }
        });
        if (file.getAiringDetails() != null) {
            aired.setText(DateFormatUtil.formatAiredDate(file.getAiringDetails().getStartTime()));
            duration.setText(DateFormatUtil.formatDurationFancy(file.getAiringDetails().getDuration()));
            channel.setText(file.getAiringDetails().getChannel() + " (" + file.getAiringDetails().getNetwork() + ")");
            firstRun.setVisible(file.getAiringDetails().isFirtRun());
            manualRecord.setVisible(file.getAiringDetails().isManualRecord());
            if (file.getAiringDetails().getYear() > 0) {
                showTitle.setText(file.getTitle() + " (" + file.getAiringDetails().getYear() + ")");
            }
        }
        watchedMarker.setVisible(file.getIsWatched().get());
        airingid.setText(file.getAiringId());
    }
