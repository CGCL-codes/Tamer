    private Manifest[] getManifests(final String[] classPath) throws ModelException {
        final ArrayList manifests = new ArrayList();
        for (int i = 0; i < classPath.length; i++) {
            final String element = classPath[i];
            if (element.endsWith(".jar") || element.startsWith("jar:")) {
                try {
                    URL url = null;
                    if (element.startsWith("jar:")) {
                        url = new URL(element);
                    } else {
                        url = new URL("jar:" + element + "!/");
                    }
                    final JarURLConnection connection = (JarURLConnection) url.openConnection();
                    final Manifest manifest = connection.getManifest();
                    if (null != manifest) {
                        manifests.add(manifest);
                    }
                } catch (final IOException ioe) {
                    final String message = REZ.getString("classloader.bad-classpath-entry.error", element);
                    throw new ModelException(message, ioe);
                }
            }
        }
        return (Manifest[]) manifests.toArray(new Manifest[0]);
    }
