    private void channelDefinition(Node channel) {
        requiredAttributesOrElements(channel, CHANNEL_DEFINITION_REQ_CHILDREN);
        allowedAttributesOrElements(channel, CHANNEL_DEFINITION_CHILDREN);
        String id = getAttributeOrChildElement(channel, ID_ATTR).trim();
        if (isValidID(id)) {
            if (config.getChannelSettings(id) != null) {
                ConfigurationException e = new ConfigurationException();
                e.setMessage(DUPLICATE_CHANNEL_ERROR, new Object[] { id });
                throw e;
            }
            ChannelSettings channelSettings = new ChannelSettings(id);
            String clientType = getAttributeOrChildElement(channel, CLASS_ATTR);
            clientType = clientType.length() > 0 ? clientType : null;
            String serverOnlyString = getAttributeOrChildElement(channel, SERVER_ONLY_ATTR);
            boolean serverOnly = serverOnlyString.length() > 0 && Boolean.valueOf(serverOnlyString).booleanValue();
            if (clientType == null && !serverOnly) {
                ConfigurationException ce = new ConfigurationException();
                ce.setMessage(CLASS_OR_SERVER_ONLY_ERROR, new Object[] { id });
                throw ce;
            } else if (clientType != null && serverOnly) {
                ConfigurationException ce = new ConfigurationException();
                ce.setMessage(CLASS_AND_SERVER_ONLY_ERROR, new Object[] { id });
                throw ce;
            } else {
                if (serverOnly) return;
                channelSettings.setClientType(clientType);
            }
            Node endpoint = selectSingleNode(channel, ENDPOINT_ELEMENT);
            if (endpoint != null) {
                allowedAttributesOrElements(endpoint, ENDPOINT_CHILDREN);
                String uri = getAttributeOrChildElement(endpoint, URL_ATTR);
                if (uri == null || EMPTY_STRING.equals(uri)) uri = getAttributeOrChildElement(endpoint, URI_ATTR);
                channelSettings.setUri(uri);
                config.addChannelSettings(id, channelSettings);
            }
            addProperty(channel, channelSettings, POLLING_ENABLED_ELEMENT);
            addProperty(channel, channelSettings, POLLING_INTERVAL_MILLIS_ELEMENT);
            addProperty(channel, channelSettings, PIGGYBACKING_ENABLED_ELEMENT);
            addProperty(channel, channelSettings, LOGIN_AFTER_DISCONNECT_ELEMENT);
            addProperty(channel, channelSettings, RECORD_MESSAGE_SIZES_ELEMENT);
            addProperty(channel, channelSettings, RECORD_MESSAGE_TIMES_ELEMENT);
            addProperty(channel, channelSettings, CONNECT_TIMEOUT_SECONDS_ELEMENT);
            addProperty(channel, channelSettings, POLLING_INTERVAL_SECONDS_ELEMENT);
            addProperty(channel, channelSettings, CLIENT_LOAD_BALANCING_ELEMENT);
            addProperty(channel, channelSettings, REQUEST_TIMEOUT_SECONDS_ELEMENT);
            NodeList properties = selectNodeList(channel, PROPERTIES_ELEMENT + "/" + SERIALIZATION_ELEMENT);
            if (properties.getLength() > 0) {
                ConfigMap map = properties(properties, getSourceFileOf(channel));
                ConfigMap serialization = map.getPropertyAsMap(SERIALIZATION_ELEMENT, null);
                if (serialization != null) {
                    String enableSmallMessages = serialization.getProperty(ENABLE_SMALL_MESSAGES_ELEMENT);
                    if (enableSmallMessages != null) {
                        ConfigMap clientMap = new ConfigMap();
                        clientMap.addProperty(ENABLE_SMALL_MESSAGES_ELEMENT, enableSmallMessages);
                        channelSettings.addProperty(SERIALIZATION_ELEMENT, clientMap);
                    }
                }
            }
        } else {
            ConfigurationException ex = new ConfigurationException();
            ex.setMessage(INVALID_ID, new Object[] { CHANNEL_DEFINITION_ELEMENT, id });
            String details = "An id must be non-empty and not contain any list delimiter characters, i.e. commas, semi-colons or colons.";
            ex.setDetails(details);
            throw ex;
        }
    }
