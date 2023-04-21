    int writeFileCount() {
        int counter = 0;
        try {
            FileInputStream fstreamWrite = new FileInputStream("InfoFiles/WriteAttributes.txt");
            DataInputStream inWrite = new DataInputStream(fstreamWrite);
            BufferedReader write = new BufferedReader(new InputStreamReader(inWrite));
            while (write.readLine() != null) {
                counter++;
            }
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counter;
    }
