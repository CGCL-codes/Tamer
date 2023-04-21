    public RingBPMTBTPVLog(long id) {
        try {
            PVLogger pvLogger = null;
            final ConnectionDictionary defaultDictionary = PVLogger.newBrowsingConnectionDictionary();
            if (defaultDictionary != null && defaultDictionary.hasRequiredInfo()) {
                pvLogger = new PVLogger(defaultDictionary);
            } else {
                ConnectionPreferenceController.displayPathPreferenceSelector();
                final ConnectionDictionary dictionary = PVLogger.newBrowsingConnectionDictionary();
                if (dictionary != null && dictionary.hasRequiredInfo()) {
                    pvLogger = new PVLogger(dictionary);
                }
            }
            if (pvLogger != null) {
                mss = pvLogger.fetchMachineSnapshot(id);
                css = mss.getChannelSnapshots();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
