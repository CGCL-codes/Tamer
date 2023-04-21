    public final List<ChannelNotification> getChannels(final Element channels, final ClassLoader cls) throws ClassNotFoundException {
        final List<ChannelNotification> l_channels = new LinkedList<ChannelNotification>();
        final Iterator<Element> it_channels = getChildrenByTagName(channels, "channel");
        try {
            while (it_channels.hasNext()) {
                final Element elemChannel = it_channels.next();
                final String className = elemChannel.getAttribute("class");
                final ChannelNotification clazz = (ChannelNotification) cls.loadClass(className).newInstance();
                if (clazz instanceof PropertiesLoader) {
                    final Iterator<Element> it_properties = getChildrenByTagName(elemChannel, "property");
                    final Map<String, Object> properties = new HashMap<String, Object>();
                    while (it_properties.hasNext()) {
                        final Element prop = it_properties.next();
                        properties.put(prop.getAttribute("name"), prop.getAttribute("value"));
                    }
                    ((PropertiesLoader) clazz).loadProperties(properties);
                }
                l_channels.add(clazz);
            }
        } catch (final InstantiationException e) {
            throw new AdapterManagementException(e);
        } catch (final IllegalAccessException e) {
            throw new AdapterManagementException(e);
        }
        return l_channels;
    }
