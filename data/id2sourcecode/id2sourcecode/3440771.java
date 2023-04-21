    public static void processUpdateDialog() {
        IPreferenceStore store = ReviewPlugin.getInstance().getPreferenceStore();
        String updateUrl = store.getString(GeneralPreferencePage.UPDATE_URL_KEY);
        if (updateUrl != null) {
            try {
                Version localVerIdentifier = getLocalPluginVersionIdentifier();
                final String localVersionId = localVerIdentifier.toString();
                URL url = new URL(updateUrl);
                Document serverDocument = parseXml(url.openStream());
                Version serverVerIdentifier = getNewestVersionIdentifier(serverDocument);
                if (serverVerIdentifier != null) {
                    final String serverVersionId = serverVerIdentifier.toString();
                    if ((serverVerIdentifier != null) && serverVerIdentifier.compareTo(localVerIdentifier) > 0) {
                        IWorkbench workbench = ReviewPlugin.getInstance().getWorkbench();
                        if (workbench.getWorkbenchWindows().length > 0) {
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    int result = ReviewDialog.processVersionCheckDialog(localVersionId, serverVersionId);
                                    if (result == MessageDialog.OK) {
                                        ReviewDialog.proccessOpenNewUpdatesWizard();
                                    }
                                }
                            });
                        }
                    }
                }
            } catch (NullPointerException e) {
                log.debug(e.getLocalizedMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.debug(e.getLocalizedMessage());
            }
        }
    }
