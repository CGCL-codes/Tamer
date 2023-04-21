    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Usage: ByteDisplay filename");
            System.exit(0);
        }
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            FileInputStream in = new FileInputStream(new File(args[0]));
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1) bytes.write(buffer, 0, read);
            System.out.println("Read " + bytes.size() + " bytes from " + args[0]);
            System.out.println(displayBytes(bytes.toByteArray()));
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
