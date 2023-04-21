    static void write_non_uniques(String filename, final Vector<String> read_names, final Vector<ReadInfo> bests) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < read_names.size(); ++i) if (!bests.elementAt(i).unique) {
                writer.write(read_names.elementAt(i));
                writer.newLine();
            }
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
