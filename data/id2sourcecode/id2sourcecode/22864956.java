    private static Collection<String> getTestClassNamesFromJar(final Bundle bundle, final String path) throws Exception {
        assert bundle != null;
        assert path != null;
        final URL url = bundle.getEntry(path);
        if (url == null) {
            return Collections.emptyList();
        }
        final JarInputStream is = new JarInputStream(url.openStream());
        final List<String> classNames = new ArrayList<String>();
        final Pattern CLASS_NAME_PATTERN = Pattern.compile("^(.*Test)\\.class$");
        try {
            JarEntry entry = null;
            while ((entry = is.getNextJarEntry()) != null) {
                final Matcher matcher = CLASS_NAME_PATTERN.matcher(entry.getName());
                if (matcher.matches()) {
                    classNames.add(matcher.group(1).replace('/', '.'));
                }
            }
        } finally {
            is.close();
        }
        return Collections.unmodifiableList(classNames);
    }
