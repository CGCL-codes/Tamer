    public void onLoad() {
        final PopupPanel waiting = Dialogs.showWaitingPopup("Loading Channels...");
        waiting.show();
        preferencesService.getChannels(new AsyncCallback<ArrayList<Channel>>() {

            public void onFailure(Throwable caught) {
                waiting.hide();
                Application.fireErrorEvent("Failed to load channels", caught);
            }

            public void onSuccess(ArrayList<Channel> result) {
                waiting.hide();
                updatePanel(result);
            }
        });
    }
