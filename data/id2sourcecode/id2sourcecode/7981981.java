    private static URLClassLoader replaceClassLoader(final URLClassLoader oldLoader, final File base, final FileFilter filter) {
        if (null != base && base.canRead() && base.isDirectory()) {
            File[] files = base.listFiles(filter);
            if (null == files || 0 == files.length) return oldLoader;
            URL[] oldElements = oldLoader.getURLs();
            URL[] elements = new URL[oldElements.length + files.length];
            System.arraycopy(oldElements, 0, elements, 0, oldElements.length);
            for (int j = 0; j < files.length; j++) {
                try {
                    URL element = files[j].toURI().normalize().toURL();
                    log.info("Adding '" + element.toString() + "' to classloader");
                    elements[oldElements.length + j] = element;
                } catch (MalformedURLException e) {
                    SolrException.log(log, "Can't add element to classloader: " + files[j], e);
                }
            }
            return URLClassLoader.newInstance(elements, oldLoader.getParent());
        }
        return oldLoader;
    }
