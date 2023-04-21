    protected void loadData() {
        synchronized (this) {
            try {
                currentData = Application.getInstance().getDataStorage().get(getDisplayedInfo());
            } catch (Exception ex) {
                Application.getInstance().getLogger().log(Level.WARNING, "Error reading TV data", ex);
            }
            if (currentData.getChannelsCount() == 0) {
                askForLoadData();
            }
        }
    }
