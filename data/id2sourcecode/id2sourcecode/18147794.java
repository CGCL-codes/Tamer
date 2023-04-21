    public void publishEvent(final ChatEvent event) {
        if (chatService != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Publishing event : " + event);
            }
            updateAutoComplete(event);
            if (isBlockedByExtendedCensor(event)) {
                return;
            }
            if (isBlockedByRegularExpressionBlocks(event)) {
                return;
            }
            event.setMessage(substituteTitles(event.getMessage(), event.getType()));
            handleOpeningTabs(event);
            processChatEventScripts(event);
            if (event.getType() == ChatType.PARTNERSHIP_DESTROYED) {
                isSimulBugConnector = false;
                simulBugPartnerName = null;
            }
            if (event.getType() == ChatType.FOLLOWING) {
                userFollowing = event.getSource();
            } else if (event.getType() == ChatType.NOT_FOLLOWING) {
                userFollowing = null;
            }
            if (event.getType() == ChatType.PARTNER_TELL) {
                playBughouseSounds(event);
                if (!event.hasSoundBeenHandled() && getPreferences().getBoolean(PreferenceKeys.BUGHOUSE_SPEAK_PARTNER_TELLS)) {
                    event.setHasSoundBeenHandled(speak(getTextAfterColon(event.getMessage())));
                }
            }
            if (event.getType() == ChatType.CHANNEL_TELL) {
                if (!event.getSource().equals(userName) && channelToSpeakTellsFrom.contains(event.getChannel())) {
                    event.setHasSoundBeenHandled(speak(IcsUtils.stripTitles(event.getSource()) + getTextAfterColon(event.getMessage())));
                }
            }
            if (event.getType() == ChatType.TELL) {
                if (isSpeakingAllPersonTells || peopleToSpeakTellsFrom.contains(event.getSource())) {
                    event.setHasSoundBeenHandled(speak(IcsUtils.stripTitles(event.getSource()) + " " + getTextAfterColon(event.getMessage())));
                }
            }
            if (event.getType() == ChatType.WHISPER || event.getType() == ChatType.KIBITZ) {
                if (!event.getSource().equals(userName) && gamesToSpeakTellsFrom.contains(event.getGameId())) {
                    event.setHasSoundBeenHandled(speak(IcsUtils.stripTitles(event.getSource()) + getTextAfterColon(event.getMessage())));
                }
            }
            int ignoreIndex = ignoringChatTypes.indexOf(event.getType());
            if (ignoreIndex != -1) {
                try {
                    ignoringChatTypes.remove(ignoreIndex);
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                }
            } else {
                chatService.publishChatEvent(event);
                processMessageCallbacks(event);
            }
        }
    }
