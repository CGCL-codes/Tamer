    private static void list(final PrintWriter out, final String[] readers, final String[] writers) {
        final String READ = "R  ";
        final String WRITE = "  W";
        final String READ_WRITE = "R/W";
        final Map<String, String> formats = new TreeMap<String, String>();
        final Map<String, String> names = new HashMap<String, String>();
        int length = 0;
        boolean secondPass = false;
        do {
            final String label = secondPass ? WRITE : READ;
            final String[] codecs = secondPass ? writers : readers;
            for (int i = 0; i < codecs.length; i++) {
                final String name = codecs[i];
                final String identifier = name.toLowerCase();
                String old = names.put(identifier, name);
                if (old != null && old.compareTo(name) > 0) {
                    names.put(identifier, old);
                }
                old = formats.put(identifier, label);
                if (old != null && old != label) {
                    formats.put(identifier, READ_WRITE);
                }
                final int lg = name.length();
                if (lg > length) {
                    length = lg;
                }
            }
        } while ((secondPass = !secondPass) == true);
        for (final Map.Entry<String, String> format : formats.entrySet()) {
            final String name = names.get(format.getKey());
            out.print("  ");
            out.print(name);
            out.print(org.geotools.resources.Utilities.spaces(length - name.length()));
            out.print(" (");
            out.print(format.getValue());
            out.println(')');
        }
    }
