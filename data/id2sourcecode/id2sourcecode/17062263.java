    public static void main(String[] args) {
        ContactManager conM = xml.readFile("./data/Test.xml");
        xml.writeFile(conM, "./data/Test_write.xml");
        ContactManager conM1 = xml.readFile("./data/Test_write.xml");
        xml.writeFile(conM1, "./data/Test_write1.xml");
        ContactManager conM2 = xml.readFile("./data/Test_write1.xml");
        xml.writeFile(conM2, "./data/Test_write2.xml");
        ContactManager conM3 = xml.readFile("./data/Test_write2.xml");
        xml.writeFile(conM3, "./data/Test_write3.xml");
    }
