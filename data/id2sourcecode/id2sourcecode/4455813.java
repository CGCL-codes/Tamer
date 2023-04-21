    private static List<String> readClassNames(final URL url) {
        assert url != null : "null url";
        final List<String> res = new LinkedList<String>();
        try {
            final InputStreamReader isr = new InputStreamReader(url.openStream());
            final BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null) {
                final int idx = line.indexOf('#');
                if (idx != -1) {
                    line = line.substring(0, idx);
                }
                line = line.trim();
                if (line.length() != 0) {
                    res.add(line);
                }
                line = br.readLine();
            }
            br.close();
            isr.close();
        } catch (final IOException ioe) {
        }
        return res;
    }
