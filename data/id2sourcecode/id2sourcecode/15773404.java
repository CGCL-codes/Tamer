    public void listen() throws ConfigurationException, IOException, MalformedContentNameStringException {
        if (_namespace.toString().startsWith("ccnx:/")) {
            UserConfiguration.setDefaultNamespacePrefix(_namespace.toString().substring(5));
        } else {
            UserConfiguration.setDefaultNamespacePrefix(_namespace.toString());
        }
        CCNHandle tempReadHandle = CCNHandle.getHandle();
        CCNHandle tempWriteHandle = CCNHandle.open();
        _readString = new CCNStringObject(_namespace, (String) null, SaveType.RAW, tempReadHandle);
        _readString.updateInBackground(true);
        String introduction = UserConfiguration.userName() + " has entered " + _namespace;
        _writeString = new CCNStringObject(_namespace, introduction, SaveType.RAW, tempWriteHandle);
        _writeString.save();
        String friendlyNameNamespaceStr = _namespaceStr + "/members/";
        _friendlyNameNamespace = KeyProfile.keyName(ContentName.fromURI(friendlyNameNamespaceStr), _writeString.getContentPublisher());
        Log.info("**** Friendly Namespace is " + _friendlyNameNamespace);
        _readNameString = new CCNStringObject(_friendlyNameNamespace, (String) null, SaveType.RAW, tempReadHandle);
        _readNameString.updateInBackground(true);
        String publishedNameStr = UserConfiguration.userName();
        Log.info("*****I am adding my own friendly name as " + publishedNameStr);
        _writeNameString = new CCNStringObject(_friendlyNameNamespace, publishedNameStr, SaveType.RAW, tempWriteHandle);
        _writeNameString.save();
        try {
            addNameToHash(_writeNameString.getContentPublisher(), _writeNameString.string());
        } catch (IOException e) {
            System.err.println("Unable to read from " + _writeNameString + "for writing to hashMap");
            e.printStackTrace();
        }
        while (!_finished) {
            try {
                synchronized (_readString) {
                    _readString.wait(CYCLE_TIME);
                }
            } catch (InterruptedException e) {
            }
            if (_readString.isSaved()) {
                Timestamp thisUpdate = _readString.getVersion();
                if ((null == _lastUpdate) || thisUpdate.after(_lastUpdate)) {
                    Log.info("Got an update: " + _readString.getVersion());
                    _lastUpdate = thisUpdate;
                    String userFriendlyName = getFriendlyName(_readString.getContentPublisher());
                    if (userFriendlyName.equals("")) {
                        String userNameStr = _namespaceStr + "/members/";
                        _friendlyNameNamespace = KeyProfile.keyName(ContentName.fromURI(userNameStr), _readString.getContentPublisher());
                        try {
                            _readNameString = new CCNStringObject(_friendlyNameNamespace, (String) null, SaveType.RAW, tempReadHandle);
                        } catch (Exception e) {
                        }
                        _readNameString.update(WAIT_TIME_FOR_FRIENDLY_NAME);
                        if (_readNameString.available()) {
                            if (!_readString.getContentPublisher().equals(_readNameString.getContentPublisher())) {
                                showMessage(_readString.getContentPublisher(), _readString.getPublisherKeyLocator(), thisUpdate, _readString.string());
                            } else {
                                addNameToHash(_readNameString.getContentPublisher(), _readNameString.string());
                                showMessage(_readNameString.string(), thisUpdate, _readString.string());
                            }
                        } else {
                            showMessage(_readString.getContentPublisher(), _readString.getPublisherKeyLocator(), thisUpdate, _readString.string());
                        }
                    } else {
                        showMessage(userFriendlyName, thisUpdate, _readString.string());
                    }
                }
            }
        }
    }
