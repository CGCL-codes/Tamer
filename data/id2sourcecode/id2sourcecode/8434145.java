    public static List<String> allResourceLines(ClassLoader classLoader, String resourceName) throws IOException {
        final List<String> result = new ArrayList<String>();
        final Enumeration<URL> resources = classLoader.getResources(resourceName);
        final Set<String> seenURLs = new HashSet<String>();
        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            final String urlString = url.toString();
            if (seenURLs.contains(urlString)) {
                continue;
            }
            seenURLs.add(urlString);
            final InputStream in = url.openStream();
            try {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    final int comment = line.indexOf('#');
                    if (comment >= 0) {
                        line = line.substring(0, comment);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        result.add(line);
                    }
                }
                reader.close();
            } finally {
                in.close();
            }
        }
        return result;
    }
