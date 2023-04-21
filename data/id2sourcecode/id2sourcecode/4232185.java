    private static BloomFilter<CharSequence> getResource(final String resourceName) {
        try {
            final URL url = WordNet.class.getClassLoader().getResource(resourceName);
            if (url == null) {
                log.info("resourceName: {} not found!", resourceName);
                return null;
            }
            final URLConnection conn = url.openConnection();
            final InputStream input = conn.getInputStream();
            final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(input));
            @SuppressWarnings("unchecked") final BloomFilter<CharSequence> filter = (BloomFilter<CharSequence>) ois.readObject();
            return filter;
        } catch (Exception e) {
            log.info("caught {}", e);
            System.err.println("caught!" + e);
            return null;
        }
    }
