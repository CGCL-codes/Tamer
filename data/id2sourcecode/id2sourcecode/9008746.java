    public static void printStream(InputStream stream) {
        Scanner in = new Scanner(stream);
        while (in.hasNextLine()) {
            System.out.println(in.nextLine());
        }
    }
