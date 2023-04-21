    public static void main(String argv[]) throws Exception {
        if (argv.length != 1 && argv.length != 2) {
            System.out.println("Usage: HtmlCharsetDetector <url> [<languageHint>]");
            System.out.println("");
            System.out.println("Where <url> is http://...");
            System.out.println("For optional <languageHint>. Use following...");
            System.out.println("		1 => Japanese");
            System.out.println("		2 => Chinese");
            System.out.println("		3 => Simplified Chinese");
            System.out.println("		4 => Traditional Chinese");
            System.out.println("		5 => Korean");
            System.out.println("		6 => Dont know (default)");
            return;
        }
        int lang = (argv.length == 2) ? Integer.parseInt(argv[1]) : nsPSMDetector.ALL;
        System.out.println("LANG = " + lang);
        nsDetector det = new nsDetector(lang);
        det.Init(new nsICharsetDetectionObserver() {

            public void Notify(String charset) {
                HtmlCharsetDetector.found = true;
                System.out.println("CHARSET = " + charset);
            }
        });
        URL url = new URL(argv[0]);
        BufferedInputStream imp = new BufferedInputStream(url.openStream());
        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;
        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            if (isAscii) isAscii = det.isAscii(buf, len);
            if (!isAscii && !done) done = det.DoIt(buf, len, false);
        }
        det.DataEnd();
        if (isAscii) {
            System.out.println("CHARSET = ASCII");
            found = true;
        }
        if (!found) {
            String prob[] = det.getProbableCharsets();
            for (int i = 0; i < prob.length; i++) {
                System.out.println("Probable Charset = " + prob[i]);
            }
        }
    }
