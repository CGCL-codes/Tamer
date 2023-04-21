    private URL findDependency(final RepositoryInfo[] repositories, final String path) {
        URL url = null;
        try {
            for (final RepositoryInfo repo : repositories) {
                url = new URL(repo.getURL().concat(path));
                if (debug) System.out.println(String.format("  -- trying %s", url.toString()));
                final URLConnection conn = url.openConnection(proxy);
                conn.connect();
                final InputStream is = conn.getInputStream();
                if (verbose) System.err.println(String.format("OK %s", url.toString()));
                break;
            }
        } catch (final Exception e) {
            url = null;
        }
        return url;
    }
