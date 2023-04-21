        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            URL translatorDir = ResourceLocatorUtil.getInstance().getTranslatorDir();
            URL url = new URL(translatorDir + "/" + locale + ".po");
            try {
                InputStream content = url.openStream();
                return new Translator(content);
            } catch (IOException e) {
                return null;
            }
        }
