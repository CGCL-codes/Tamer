    public static void dumpPart(Part p) throws Exception {
        if (p instanceof Message) dumpEnvelope((Message) p);
        pr("CONTENT-TYPE: " + p.getContentType());
        if (p.isMimeType("text/plain")) {
            pr("This is plain text");
            pr("---------------------------");
            if (!showStructure) System.out.println((String) p.getContent());
        } else if (p.isMimeType("multipart/*")) {
            pr("This is a Multipart");
            pr("---------------------------");
            Multipart mp = (Multipart) p.getContent();
            level++;
            int count = mp.getCount();
            for (int i = 0; i < count; i++) dumpPart(mp.getBodyPart(i));
            level--;
        } else if (p.isMimeType("message/rfc822")) {
            pr("This is a Nested Message");
            pr("---------------------------");
            level++;
            dumpPart((Part) p.getContent());
            level--;
        } else if (!showStructure) {
            Object o = p.getContent();
            if (o instanceof String) {
                pr("This is a string");
                pr("---------------------------");
                System.out.println((String) o);
            } else if (o instanceof InputStream) {
                pr("This is just an input stream");
                pr("---------------------------");
                InputStream is = (InputStream) o;
                int c;
                while ((c = is.read()) != -1) System.out.write(c);
            } else {
                pr("This is an unknown type");
                pr("---------------------------");
                pr(o.toString());
            }
        } else {
            pr("This is an unknown type");
            pr("---------------------------");
        }
    }
