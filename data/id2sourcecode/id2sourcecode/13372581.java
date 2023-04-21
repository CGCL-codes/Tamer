    public static void main(String[] args) throws IOException {
        if (args.length == 2 && args[0].equals("encode")) System.out.println(encode(getBytes(args[1]))); else if (args.length == 2 && args[0].equals("decode")) System.out.write(decode(args[1])); else if (args.length == 2 && args[0].equals("encodefile")) {
            FileInputStream fis = new FileInputStream(args[1]);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int n;
            while ((n = fis.read(buf)) != -1) baos.write(buf, 0, n);
            fis.close();
            System.out.println(encode(baos.toByteArray()));
        } else {
            System.out.println("Usage: Base64 {encode,decode} filename");
            System.exit(1);
        }
        System.exit(0);
    }
