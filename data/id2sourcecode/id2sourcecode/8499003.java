    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("Usage: MIMEMessage parse filename");
            System.out.println(" or    MIMEMessage create file1..fileN");
            System.exit(0);
        }
        try {
            String cmd = args[0];
            if (cmd.equalsIgnoreCase("parse")) {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(args[1])));
                long time = System.currentTimeMillis();
                MIMEMessage m = MIMEMessage.parse(in);
                time = System.currentTimeMillis() - time;
                System.err.println("Parse took " + time + "ms");
                System.err.println("");
                Enumeration e = m.getAttachments();
                while (e.hasMoreElements()) {
                    MIMEAttachment a = (MIMEAttachment) e.nextElement();
                    System.err.println("Attachment:");
                    System.err.println("  Headers:");
                    Enumeration h = a.getHeaderNames();
                    while (h.hasMoreElements()) {
                        String header = (String) h.nextElement();
                        System.err.println("    " + header + ": " + a.getHeader(header));
                    }
                    System.err.println("  Info:");
                    System.err.println("    Content length: " + a.getContent().length);
                    System.err.println("    Binary:         " + a.isBinary());
                    System.err.println("");
                }
                System.out.println(m);
            } else {
                System.err.println("Creating new MIMEMessage");
                MIMEMessage m = new MIMEMessage();
                for (int i = 1; i < args.length; i++) {
                    String file = args[i];
                    String type = "unknown";
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(file)));
                    byte[] buffer = new byte[8192];
                    int read = 0;
                    while ((read = in.read(buffer)) != -1) bout.write(buffer, 0, read);
                    byte[] data = bout.toByteArray();
                    boolean binary = isBinaryContent(data);
                    MIMEAttachment a = new MIMEAttachment(type, file, data, binary);
                    System.err.println("binary = " + binary);
                    m.addAttachment(a);
                }
                System.out.println(m);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
