    public TestResultLogger(String webRoot, String fileName) {
        try {
            writer = new FileWriter(fileName);
            writer.write("Web-Root" + "," + webRoot + "\n");
            writer.write("Thread");
            writer.write(",");
            writer.write("Iteration");
            writer.write(",");
            writer.write("Start Time");
            writer.write(",");
            writer.write("End Time");
            writer.write(",");
            writer.write("Duration");
            writer.write(",");
            writer.write("Test Case");
            writer.write(",");
            writer.write("Status");
            writer.write("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
