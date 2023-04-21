    private void scanInputStream(URL url, List types, List services) {
        try {
            Object object = url.openStream();
            if (object != null) {
                JarInputStream stream = new JarInputStream((InputStream) object);
                scanJarInputStream(stream, types, services);
                return;
            } else {
                if (getLogger().isWarnEnabled()) {
                    final String warning = REZ.getString("scanner.stream.unrecognized-content.warning", url.toString());
                    getLogger().warn(warning);
                }
            }
        } catch (Throwable e) {
            if (getLogger().isWarnEnabled()) {
                final String error = REZ.getString("scanner.stream.content.error", url.toString());
                final String warning = ExceptionHelper.packException(error, e, getLogger().isDebugEnabled());
                getLogger().warn(warning);
            }
        }
    }
