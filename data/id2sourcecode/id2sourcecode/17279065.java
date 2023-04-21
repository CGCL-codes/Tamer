    private void dumpBody(Part p) throws Exception {
        printOut("=================================================================");
        printOut("CONTENT-TYPE: " + p.getContentType());
        if (p.isMimeType("text/plain")) {
            printOut("Plain text ---------------------------");
            System.out.println((String) p.getContent());
        } else if (p.getContentType().toUpperCase().startsWith("TEXT")) {
            printOut("Other text ---------------------------");
            System.out.println((String) p.getContent());
        } else if (p.isMimeType("multipart/*")) {
            printOut("Multipart ---------------------------");
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) dumpBody(mp.getBodyPart(i));
        } else if (p.isMimeType("message/rfc822")) {
            printOut("Nested ---------------------------");
            dumpBody((Part) p.getContent());
        } else {
            Object o = p.getContent();
            if (o instanceof String) {
                printOut("This is a string ---------------------------");
                System.out.println((String) o);
            } else if (o instanceof InputStream) {
                printOut("This is just an input stream ---------------------------");
                InputStream is = (InputStream) o;
                int c;
                while ((c = is.read()) != -1) System.out.write(c);
            } else {
                printOut("This is an unknown type ---------------------------");
                printOut(o.toString());
            }
        }
        printOut("=================================================================");
    }
