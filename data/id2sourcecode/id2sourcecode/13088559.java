    protected static void test1() {
        try {
            OutputStream os = new InputOutputStream();
            Writer writer = new OutputStreamWriter(os);
            writer.write("Lourival Sabino");
            writer.close();
            InputStream input = ((InputOutputStream) os).getInputStream();
            Scanner scanner = new Scanner(input);
            System.out.println(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
