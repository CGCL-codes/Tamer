    private void processInput(BufferedReader in, PrintWriter out) throws IOException {
        do {
            String nextLine = in.readLine();
            out.println(nextLine);
            out.flush();
        } while (true);
    }
