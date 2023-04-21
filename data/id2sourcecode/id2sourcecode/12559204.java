    void saveResult() throws IOException {
        BufferedWriter file = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out.txt")));
        for (int i = 0; i < graph.getSize(); i++) {
            file.write(graph.getVertex(i).getName() + " " + threads.get(resultThreadIndex).getResult().get(i));
            file.newLine();
        }
        file.close();
    }
