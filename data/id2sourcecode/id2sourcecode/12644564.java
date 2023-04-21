    private MessageProvider(Locale locale) throws IOException {
        final URL url = ModelLoader.solveResource("messages/messages_" + locale);
        if (url == null) {
            throw new IOException("invalid URL");
        } else {
            final InputStream inputStream = url.openStream();
            try {
                properties = new Properties();
                properties.load(inputStream);
            } finally {
                inputStream.close();
            }
        }
    }
