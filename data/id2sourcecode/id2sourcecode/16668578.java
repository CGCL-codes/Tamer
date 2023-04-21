    public static void main(String[] args) {
        ContactManager conM = csv.readFile("./data/Test.csv");
        csv.writeFile(conM, "./data/Test_write.csv");
        ContactManager conM1 = csv.readFile("./data/Test_write.csv");
        csv.writeFile(conM1, "./data/Test_write1.csv");
    }
