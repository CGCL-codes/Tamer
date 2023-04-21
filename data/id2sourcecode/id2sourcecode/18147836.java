    protected void handleOpeningTabs(final ChatEvent event) {
        if (!isConnected()) {
            return;
        }
        ThreadService.getInstance().run(new Runnable() {

            public void run() {
                if (getPreferences().getBoolean(PreferenceKeys.CHAT_OPEN_PERSON_TAB_ON_PERSON_TELLS) && event.getType() == ChatType.TELL) {
                    ChatUtils.openPersonTab(IcsConnector.this, event.getSource(), false);
                } else if (getPreferences().getBoolean(PreferenceKeys.CHAT_OPEN_CHANNEL_TAB_ON_CHANNEL_TELLS) && event.getType() == ChatType.CHANNEL_TELL) {
                    ChatUtils.openChannelTab(IcsConnector.this, event.getChannel(), false);
                } else if (getPreferences().getBoolean(PreferenceKeys.CHAT_OPEN_PARTNER_TAB_ON_PTELLS) && event.getType() == ChatType.PARTNER_TELL) {
                    ChatUtils.openPartnerTab(IcsConnector.this, false);
                }
            }

            public String toString() {
                return "IcsConnector.handleOpeningTabs runnable";
            }
        });
    }
