    public static void main(String[] av) {
        final String filename = av[0];
        JTail jtail = new JTail();
        try {
            final File file = new File(filename);
            final FileInputStream fis = new FileInputStream(filename);
            long length = file.length();
            final long skip = length - 5 * 80;
            if (0 < skip) {
                fis.skip(skip);
            }
            final FileChannel chan = fis.getChannel();
            jtail.doTailChannel(new DefaultCallback(), chan);
        } catch (Exception e) {
            System.err.println("failed: " + e.toString());
            log.debug(e.toString(), e);
        }
    }
