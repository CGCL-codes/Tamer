    public static void dumpPart(Part p) throws Exception {
        if (p instanceof Message) {
            Message m = (Message) p;
            Address[] a;
            if ((a = m.getFrom()) != null) {
                for (int j = 0; j < a.length; j++) System.out.println("FROM: " + a[j].toString());
            }
            if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
                for (int j = 0; j < a.length; j++) System.out.println("TO: " + a[j].toString());
            }
            System.out.println("SUBJECT: " + m.getSubject());
            Date d = m.getSentDate();
            System.out.println("SendDate: " + (d != null ? d.toLocaleString() : "UNKNOWN"));
            Flags flags = m.getFlags();
            StringBuffer sb = new StringBuffer();
            Flags.Flag[] sf = flags.getSystemFlags();
            boolean first = true;
            for (int i = 0; i < sf.length; i++) {
                String s;
                Flags.Flag f = sf[i];
                if (f == Flags.Flag.ANSWERED) s = "\\Answered"; else if (f == Flags.Flag.DELETED) s = "\\Deleted"; else if (f == Flags.Flag.DRAFT) s = "\\Draft"; else if (f == Flags.Flag.FLAGGED) s = "\\Flagged"; else if (f == Flags.Flag.RECENT) s = "\\Recent"; else if (f == Flags.Flag.SEEN) s = "\\Seen"; else continue;
                if (first) first = false; else sb.append(' ');
                sb.append(s);
            }
            String[] uf = flags.getUserFlags();
            for (int i = 0; i < uf.length; i++) {
                if (first) first = false; else sb.append(' ');
                sb.append(uf[i]);
            }
            System.out.println("FLAGS = " + sb.toString());
        }
        System.out.println("CONTENT-TYPE: " + p.getContentType());
        Object o = p.getContent();
        if (o instanceof String) {
            System.out.println("This is a String");
            System.out.println((String) o);
        } else if (o instanceof Multipart) {
            System.out.println("This is a Multipart");
            Multipart mp = (Multipart) o;
            int count = mp.getCount();
            for (int i = 0; i < count; i++) dumpPart(mp.getBodyPart(i));
        } else if (o instanceof InputStream) {
            System.out.println("This is just an input stream");
            InputStream is = (InputStream) o;
            int c;
            while ((c = is.read()) != -1) System.out.write(c);
        }
    }
