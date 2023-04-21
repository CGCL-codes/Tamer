    public SVNProperties compareTo(SVNWCProperties properties) throws SVNException {
        final SVNProperties locallyChangedProperties = new SVNProperties();
        compareTo(properties, new ISVNPropertyComparator() {

            public void propertyAdded(String name, InputStream value, int length) {
                propertyChanged(name, value, length);
            }

            public void propertyChanged(String name, InputStream newValue, int length) {
                ByteArrayOutputStream os = new ByteArrayOutputStream(length);
                for (int i = 0; i < length; i++) {
                    try {
                        os.write(newValue.read());
                    } catch (IOException e) {
                    }
                }
                byte[] bytes = os.toByteArray();
                try {
                    locallyChangedProperties.put(name, new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    locallyChangedProperties.put(name, new String(bytes));
                }
            }

            public void propertyDeleted(String name) {
                locallyChangedProperties.put(name, (SVNPropertyValue) null);
            }
        });
        return locallyChangedProperties;
    }
