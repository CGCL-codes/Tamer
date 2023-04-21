    public static void main(String args[]) throws FileNotFoundException {
        InputStream in = new FileInputStream("D:/work/AutoSubmit.java");
        Scanner scanner = new Scanner(in);
        int number = 1;
        while (scanner.hasNextLine()) {
            System.out.print(number + ".  ");
            System.out.println(scanner.nextLine());
            number++;
        }
    }
