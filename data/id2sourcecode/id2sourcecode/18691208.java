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
