    public static synchronized String[][] parse() {
        final StringList langs = new StringList();
        final StringList creds = new StringList();
        try {
            final URL url = BaseX.class.getResource(SUFFIX);
            if (url.getProtocol().equals("jar")) {
                final JarURLConnection conn = (JarURLConnection) url.openConnection();
                final String pre = conn.getEntryName();
                final JarFile jar = conn.getJarFile();
                final Enumeration<JarEntry> je = jar.entries();
                while (je.hasMoreElements()) {
                    final JarEntry entry = je.nextElement();
                    final String name = entry.getName();
                    if (!name.startsWith(pre) || !name.endsWith(SUFFIX)) continue;
                    final byte[] cont = new byte[(int) entry.getSize()];
                    jar.getInputStream(entry).read(cont);
                    langs.add(name.replaceAll(".*/|." + SUFFIX, ""));
                    creds.add(credits(cont));
                }
            } else {
                for (final File f : new File(url.getFile()).listFiles()) {
                    langs.add(f.getName().replaceAll("." + SUFFIX, ""));
                    creds.add(credits(new IOFile(f).content()));
                }
            }
        } catch (final IOException ex) {
            Main.errln(ex);
        }
        return new String[][] { langs.finish(), creds.finish() };
    }
