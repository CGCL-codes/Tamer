    void write_non_uniques(String filename, final Vector<Integer> ambigs, final Vector<String> read_names) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < ambigs.size(); ++i) writer.write(read_names.elementAt(ambigs.elementAt(i)) + "\n");
        } catch (IOException ioe) {
            System.out.println("Error while closing the stream : " + ioe);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing the stream : " + ioe);
            }
        }
    }
