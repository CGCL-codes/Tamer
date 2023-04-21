    protected void digest(final CommandLine line) throws Exception {
        final DigestAlgorithm digest = DigestAlgorithm.newInstance(line.getOptionValue(OPT_ALG));
        if (line.hasOption(OPT_SALT)) {
            digest.setSalt(hexConv.toBytes(line.getOptionValue(OPT_SALT)));
        }
        byte[] hash = null;
        final InputStream in = getInputStream(line);
        try {
            hash = digest.digest(in);
        } finally {
            closeStream(in);
        }
        if (line.hasOption(OPT_ENCODING)) {
            final String encName = line.getOptionValue(OPT_ENCODING);
            Converter conv = null;
            if (BASE_64_ENCODING.equals(encName)) {
                conv = new Base64Converter();
            } else if (HEX_ENCODING.equals(encName)) {
                conv = hexConv;
            } else {
                throw new IllegalArgumentException("Unknown encoding.");
            }
            System.out.println(conv.fromBytes(hash));
        } else {
            System.out.print(hash);
        }
    }
