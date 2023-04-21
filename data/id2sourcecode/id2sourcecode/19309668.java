    public static Set getAvailableLocales() {
        URL url = null;
        Locale locale = null;
        InputStream stream = null;
        final Set result = new HashSet();
        try {
            final URL location = getDictionaryLocation();
            if (location == null) return Collections.EMPTY_SET;
            final Locale[] locales = Locale.getAvailableLocales();
            for (int index = 0; index < locales.length; index++) {
                locale = locales[index];
                url = new URL(location, locale.toString().toLowerCase() + ".dictionary");
                try {
                    stream = url.openStream();
                    if (stream != null) {
                        try {
                            result.add(locale);
                        } finally {
                            stream.close();
                        }
                    }
                } catch (IOException exception) {
                }
            }
        } catch (MalformedURLException exception) {
        }
        return result;
    }
