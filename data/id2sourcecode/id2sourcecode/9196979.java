    public final void testRead() {
        DefaultWriter writer = new DefaultWriter(inputFilename, inputFilename);
        try {
            writer.read();
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        } catch (BiffException be) {
            System.out.println("Error: " + be.getMessage());
        }
    }
