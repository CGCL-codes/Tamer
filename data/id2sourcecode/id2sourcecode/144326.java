    public static void main(String[] args) throws Exception {
        Pattern p = Pattern.compile("//.*$", Pattern.MULTILINE);
        for (int i = 0; i < args.length; i++) {
            File f = new File(args[i]);
            FileInputStream fis = new FileInputStream(f);
            FileChannel fc = fis.getChannel();
            ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
            Charset cs = Charset.forName("8859_1");
            CharsetDecoder cd = cs.newDecoder();
            CharBuffer cb = cd.decode(bb);
            Matcher m = p.matcher(cb);
            while (m.find()) System.out.println("Found: " + m.group());
        }
    }
