    protected static void test2() {
        try {
            Writer writer = new ReaderWriter();
            writer.write("Lourival Sabino XXX");
            Scanner scanner = new Scanner(((ReaderWriter) writer).getReader());
            System.out.println(scanner.nextLine());
            writer = new ReaderWriter();
            writer.write("Lourival Sabino YYY");
            scanner = new Scanner(((ReaderWriter) writer).getReader());
            System.out.println(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
