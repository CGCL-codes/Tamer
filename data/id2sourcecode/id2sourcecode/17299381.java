    private void service(Node service) {
        requiredAttributesOrElements(service, SERVICE_REQ_CHILDREN);
        allowedAttributesOrElements(service, SERVICE_CHILDREN);
        String id = getAttributeOrChildElement(service, ID_ATTR);
        if (isValidID(id)) {
            ServiceSettings serviceSettings = config.getServiceSettings(id);
            if (serviceSettings == null) {
                serviceSettings = new ServiceSettings(id);
                serviceSettings.setSourceFile(getSourceFileOf(service));
                config.addServiceSettings(serviceSettings);
            } else {
                ConfigurationException e = new ConfigurationException();
                e.setMessage(DUPLICATE_SERVICE_ERROR, new Object[] { id });
                throw e;
            }
            String className = getAttributeOrChildElement(service, CLASS_ATTR);
            if (className.length() > 0) {
                serviceSettings.setClassName(className);
                if (className.equals("flex.messaging.services.AdvancedMessagingSupport")) advancedMessagingSupportRegistered = true;
            } else {
                ConfigurationException ex = new ConfigurationException();
                ex.setMessage(CLASS_NOT_SPECIFIED, new Object[] { SERVICE_ELEMENT, id });
                throw ex;
            }
            NodeList properties = selectNodeList(service, PROPERTIES_ELEMENT + "/*");
            if (properties.getLength() > 0) {
                ConfigMap map = properties(properties, getSourceFileOf(service));
                serviceSettings.addProperties(map);
            }
            Node defaultChannels = selectSingleNode(service, DEFAULT_CHANNELS_ELEMENT);
            if (defaultChannels != null) {
                allowedChildElements(defaultChannels, DEFAULT_CHANNELS_CHILDREN);
                NodeList channels = selectNodeList(defaultChannels, CHANNEL_ELEMENT);
                for (int c = 0; c < channels.getLength(); c++) {
                    Node chan = channels.item(c);
                    allowedAttributes(chan, new String[] { REF_ATTR });
                    defaultChannel(chan, serviceSettings);
                }
            } else if (config.getDefaultChannels().size() > 0) {
                for (Iterator iter = config.getDefaultChannels().iterator(); iter.hasNext(); ) {
                    String channelId = (String) iter.next();
                    ChannelSettings channel = config.getChannelSettings(channelId);
                    serviceSettings.addDefaultChannel(channel);
                }
            }
            Node defaultSecurityConstraint = selectSingleNode(service, DEFAULT_SECURITY_CONSTRAINT_ELEMENT);
            if (defaultSecurityConstraint != null) {
                requiredAttributesOrElements(defaultSecurityConstraint, new String[] { REF_ATTR });
                allowedAttributesOrElements(defaultSecurityConstraint, new String[] { REF_ATTR });
                String ref = getAttributeOrChildElement(defaultSecurityConstraint, REF_ATTR);
                if (ref.length() > 0) {
                    SecurityConstraint sc = ((MessagingConfiguration) config).getSecuritySettings().getConstraint(ref);
                    if (sc == null) {
                        ConfigurationException e = new ConfigurationException();
                        e.setMessage(REF_NOT_FOUND, new Object[] { SECURITY_CONSTRAINT_DEFINITION_ELEMENT, ref });
                        throw e;
                    }
                    serviceSettings.setConstraint(sc);
                } else {
                    ConfigurationException ex = new ConfigurationException();
                    ex.setMessage(INVALID_SECURITY_CONSTRAINT_REF, new Object[] { ref, id });
                    throw ex;
                }
            }
            Node adapters = selectSingleNode(service, ADAPTERS_ELEMENT);
            if (adapters != null) {
                allowedChildElements(adapters, ADAPTERS_CHILDREN);
                NodeList serverAdapters = selectNodeList(adapters, ADAPTER_DEFINITION_ELEMENT);
                for (int a = 0; a < serverAdapters.getLength(); a++) {
                    Node adapter = serverAdapters.item(a);
                    adapterDefinition(adapter, serviceSettings);
                }
            }
            NodeList list = selectNodeList(service, DESTINATION_ELEMENT);
            for (int i = 0; i < list.getLength(); i++) {
                Node dest = list.item(i);
                destination(dest, serviceSettings);
            }
            list = selectNodeList(service, DESTINATION_INCLUDE_ELEMENT);
            for (int i = 0; i < list.getLength(); i++) {
                Node dest = list.item(i);
                destinationInclude(dest, serviceSettings);
            }
        } else {
            ConfigurationException ex = new ConfigurationException();
            ex.setMessage(INVALID_ID, new Object[] { SERVICE_ELEMENT, id });
            throw ex;
        }
    }
