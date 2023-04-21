    private Set<String> findClassNamesFromJarURL(final URL url, final Class<?> tosubclass, final String starts) {
        JarURLConnection conn = null;
        JarFile jfile = null;
        final SortedSet<String> rval = new TreeSet<String>();
        try {
            conn = (JarURLConnection) url.openConnection();
            jfile = conn.getJarFile();
            rval.addAll(findClassNamesFromJarConnection(jfile.entries(), tosubclass, starts));
        } catch (IOException ioex) {
            JOptionPane.showMessageDialog(null, ioex.getMessage(), rtsiName, JOptionPane.ERROR_MESSAGE);
        }
        return rval;
    }
