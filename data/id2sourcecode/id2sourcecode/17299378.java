    private void channelDefinition(Node channel) {
        requiredAttributesOrElements(channel, CHANNEL_DEFINITION_REQ_CHILDREN);
        allowedAttributesOrElements(channel, CHANNEL_DEFINITION_CHILDREN);
        String id = getAttributeOrChildElement(channel, ID_ATTR);
        if (isValidID(id)) {
            if (config.getChannelSettings(id) != null) {
                ConfigurationException e = new ConfigurationException();
                e.setMessage(DUPLICATE_CHANNEL_ERROR, new Object[] { id });
                throw e;
            }
            ChannelSettings channelSettings = new ChannelSettings(id);
            channelSettings.setSourceFile(getSourceFileOf(channel));
            String clientType = getAttributeOrChildElement(channel, CLASS_ATTR);
            clientType = clientType.length() > 0 ? clientType : null;
            String serverOnlyString = getAttributeOrChildElement(channel, SERVER_ONLY_ATTR);
            boolean serverOnly = serverOnlyString.length() > 0 ? Boolean.valueOf(serverOnlyString) : false;
            if (clientType == null && !serverOnly) {
                ConfigurationException ce = new ConfigurationException();
                ce.setMessage(CLASS_OR_SERVER_ONLY_ERROR, new Object[] { id });
                throw ce;
            } else if (clientType != null && serverOnly) {
                ConfigurationException ce = new ConfigurationException();
                ce.setMessage(CLASS_AND_SERVER_ONLY_ERROR, new Object[] { id });
                throw ce;
            } else {
                if (serverOnly) channelSettings.setServerOnly(true); else channelSettings.setClientType(clientType);
            }
            String remote = getAttributeOrChildElement(channel, REMOTE_ATTR);
            channelSettings.setRemote(Boolean.valueOf(remote));
            Node endpoint = selectSingleNode(channel, ENDPOINT_ELEMENT);
            if (endpoint != null) {
                allowedAttributesOrElements(endpoint, ENDPOINT_CHILDREN);
                String type = getAttributeOrChildElement(endpoint, CLASS_ATTR);
                channelSettings.setEndpointType(type);
                String uri = getAttributeOrChildElement(endpoint, URL_ATTR);
                if (uri == null || EMPTY_STRING.equals(uri)) uri = getAttributeOrChildElement(endpoint, URI_ATTR);
                channelSettings.setUri(uri);
                config.addChannelSettings(id, channelSettings);
            }
            Node server = selectSingleNode(channel, SERVER_ELEMENT);
            if (server != null) {
                requiredAttributesOrElements(server, CHANNEL_DEFINITION_SERVER_REQ_CHILDREN);
                String serverId = getAttributeOrChildElement(server, REF_ATTR);
                channelSettings.setServerId(serverId);
            }
            NodeList properties = selectNodeList(channel, PROPERTIES_ELEMENT + "/*");
            if (properties.getLength() > 0) {
                ConfigMap map = properties(properties, getSourceFileOf(channel));
                channelSettings.addProperties(map);
                if (!verifyAdvancedMessagingSupport) {
                    ConfigMap outboundQueueProcessor = map.getPropertyAsMap(FLEX_CLIENT_OUTBOUND_QUEUE_PROCESSOR_ELEMENT, null);
                    if (outboundQueueProcessor != null) {
                        ConfigMap queueProcessorProperties = outboundQueueProcessor.getPropertyAsMap(PROPERTIES_ELEMENT, null);
                        if (queueProcessorProperties != null) {
                            boolean adaptiveFrequency = queueProcessorProperties.getPropertyAsBoolean(ADAPTIVE_FREQUENCY, false);
                            if (adaptiveFrequency) verifyAdvancedMessagingSupport = true;
                        }
                    }
                }
            }
            String ref = evaluateExpression(channel, "@" + SECURITY_CONSTRAINT_ATTR).toString().trim();
            if (ref.length() > 0) {
                SecurityConstraint sc = ((MessagingConfiguration) config).getSecuritySettings().getConstraint(ref);
                if (sc != null) {
                    channelSettings.setConstraint(sc);
                } else {
                    ConfigurationException ex = new ConfigurationException();
                    ex.setMessage(REF_NOT_FOUND_IN_CHANNEL, new Object[] { SECURITY_CONSTRAINT_ATTR, ref, id });
                    throw ex;
                }
            } else {
                Node security = selectSingleNode(channel, SECURITY_ELEMENT);
                if (security != null) {
                    allowedChildElements(security, EMBEDDED_SECURITY_CHILDREN);
                    Node constraint = selectSingleNode(security, SECURITY_CONSTRAINT_ELEMENT);
                    if (constraint != null) {
                        SecurityConstraint sc = securityConstraint(constraint, true);
                        channelSettings.setConstraint(sc);
                    }
                }
            }
        } else {
            ConfigurationException ex = new ConfigurationException();
            ex.setMessage(INVALID_ID, new Object[] { CHANNEL_DEFINITION_ELEMENT, id });
            ex.setDetails(INVALID_ID);
            throw ex;
        }
    }
