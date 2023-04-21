    public void initialize(PluginInterface pi) throws PluginException {
        pluginInterface = pi;
        pi.addListener(new PluginListener() {

            public void initializationComplete() {
            }

            public void closedownInitiated() {
            }

            public void closedownComplete() {
            }
        });
        loggerChannel = pi.getLogger().getChannel("aztsearch");
        initPluginViewModel(pi);
        if (!initPluginConfigModel(pi)) return;
        pi.getUIManager().addUIListener(new UIManagerListener() {

            public void UIAttached(UIInstance instance) {
                if (instance instanceof UISWTInstance) {
                    swtInstance = ((UISWTInstance) instance);
                    viewListener = new TSViewListener();
                    if (viewListener != null) {
                        swtInstance.addView(UISWTInstance.VIEW_MAIN, VIEWID, viewListener);
                        if (autoOpenPlugin) {
                            swtInstance.openMainView(VIEWID, viewListener, null);
                        }
                    }
                }
            }

            public void UIDetached(UIInstance instance) {
                if (instance instanceof UISWTInstance) swtInstance = null;
            }
        });
    }
