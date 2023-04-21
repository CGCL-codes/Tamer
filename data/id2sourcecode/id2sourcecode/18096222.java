    @Test
    public void test() {
        String test = "test test tess aaaa";
        String filename = "./filename.txt";
        writeAndReadToFile.writeToFile(filename, test);
        String out = "";
        out = writeAndReadToFile.readFile("./filename.txt", "");
        System.out.println("out  " + out);
    }
