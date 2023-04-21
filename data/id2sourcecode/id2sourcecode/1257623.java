    public static InputStream getInputStream(String fileName) throws FileNotFoundException, IOException, NoClassDefFoundError {
        try {
            URL urlAttempt = new URL(fileName);
            PushbackInputStream test = new PushbackInputStream(urlAttempt.openStream());
            int testchar = test.read();
            test.unread(testchar);
            if (testchar == 0x1f) {
                try {
                    GZIPInputStream gzi = new GZIPInputStream(test);
                    return (new BufferedInputStream(gzi));
                } catch (Exception e) {
                    String lm = e.getLocalizedMessage();
                    if ((lm != null) && (lm.compareTo("Not in GZIP format") == 0)) {
                        System.out.println("File not in GZIP format");
                    }
                }
                return (null);
            } else {
                return (new BufferedInputStream(test));
            }
        } catch (Exception e) {
            File source = new File(fileName);
            return (getInputStream(source));
        }
    }
