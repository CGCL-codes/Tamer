    protected void initialize() {
        try {
            ClassLoader[] classLoaders = getClassLoaders();
            for (ClassLoader classLoader : classLoaders) {
                Enumeration providerEnum = classLoader.getResources("META-INF/smack.providers");
                while (providerEnum.hasMoreElements()) {
                    URL url = (URL) providerEnum.nextElement();
                    InputStream providerStream = null;
                    try {
                        providerStream = url.openStream();
                        XmlPullParser parser = new MXParser();
                        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                        parser.setInput(providerStream, "UTF-8");
                        int eventType = parser.getEventType();
                        do {
                            if (eventType == XmlPullParser.START_TAG) {
                                if (parser.getName().equals("iqProvider")) {
                                    parser.next();
                                    parser.next();
                                    String elementName = parser.nextText();
                                    parser.next();
                                    parser.next();
                                    String namespace = parser.nextText();
                                    parser.next();
                                    parser.next();
                                    String className = parser.nextText();
                                    String key = getProviderKey(elementName, namespace);
                                    if (!iqProviders.containsKey(key)) {
                                        try {
                                            Class provider = Class.forName(className);
                                            if (IQProvider.class.isAssignableFrom(provider)) {
                                                iqProviders.put(key, provider.newInstance());
                                            } else if (IQ.class.isAssignableFrom(provider)) {
                                                iqProviders.put(key, provider);
                                            }
                                        } catch (ClassNotFoundException cnfe) {
                                            cnfe.printStackTrace();
                                        }
                                    }
                                } else if (parser.getName().equals("extensionProvider")) {
                                    parser.next();
                                    parser.next();
                                    String elementName = parser.nextText();
                                    parser.next();
                                    parser.next();
                                    String namespace = parser.nextText();
                                    parser.next();
                                    parser.next();
                                    String className = parser.nextText();
                                    String key = getProviderKey(elementName, namespace);
                                    if (!extensionProviders.containsKey(key)) {
                                        try {
                                            Class provider = Class.forName(className);
                                            if (PacketExtensionProvider.class.isAssignableFrom(provider)) {
                                                extensionProviders.put(key, provider.newInstance());
                                            } else if (PacketExtension.class.isAssignableFrom(provider)) {
                                                extensionProviders.put(key, provider);
                                            }
                                        } catch (ClassNotFoundException cnfe) {
                                            cnfe.printStackTrace();
                                        }
                                    }
                                }
                            }
                            eventType = parser.next();
                        } while (eventType != XmlPullParser.END_DOCUMENT);
                    } finally {
                        try {
                            providerStream.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
