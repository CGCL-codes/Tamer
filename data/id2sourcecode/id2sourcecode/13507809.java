    public Preferences(IPreferences prefs) {
        tangoHostName = prefs.getTangoHostName();
        tangoHostPort = prefs.getTangoHostPort();
        sardanaFilter = prefs.getPoolFilter();
        hideInternalMotorGroups = prefs.hideInternalMotorGroups();
        poolPropSaveLevel = prefs.getPoolPropSaveLevel();
        controllerPropSaveLevel = prefs.getControllerPropSaveLevel();
        pseudoMotorPropSaveLevel = prefs.getPseudoMotorPropSaveLevel();
        motorAttrSaveLevel = prefs.getMotorAttributeSaveLevel();
        channelAttrSaveLevel = prefs.getChannelAttributeSaveLevel();
    }
