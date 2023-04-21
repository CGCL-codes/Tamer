    public static void main(String[] args0) throws IOException {
        Character mask = null;
        String trigger = null;
        String[] args;
        ConsoleReader reader = new ConsoleReader();
        reader.setBellEnabled(false);
        reader.setDebug(new PrintWriter(new FileWriter("writer.debug", true)));
        if ((args0 == null) || (args0.length == 0)) {
            usage();
            args = new String[] { "dictionary" };
        } else {
            args = args0;
        }
        List completors = new LinkedList();
        if (args.length > 0) {
            if (args[0].equals("none")) {
            } else if (args[0].equals("files")) {
                completors.add(new FileNameCompletor());
            } else if (args[0].equals("classes")) {
                completors.add(new ClassNameCompletor());
            } else if (args[0].equals("dictionary")) {
                completors.add(new SimpleCompletor(new GZIPInputStream(TestJline.class.getResourceAsStream("english.gz"))));
            } else if (args[0].equals("simple")) {
                completors.add(new SimpleCompletor(new String[] { "foo", "bar", "baz" }));
            } else {
                usage();
                return;
            }
        }
        if (args.length == 3) {
            mask = new Character(args[2].charAt(0));
            trigger = args[1];
        }
        reader.addCompletor(new ArgumentCompletor(completors));
        String line;
        PrintWriter out = new PrintWriter(System.out);
        while ((line = reader.readLine("prompt> ")) != null) {
            out.println("======>\"" + line + "\"");
            out.flush();
            if ((trigger != null) && (line.compareTo(trigger) == 0)) {
                line = reader.readLine("password> ", mask);
            }
            if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                break;
            }
        }
    }
