    public void printStatus(OutputStream os) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.write("--- SERVER STATUS ---");
        bw.newLine();
        bw.write(" - status: ");
        bw.write(running ? "running" : "stopped");
        bw.newLine();
        bw.write(" - connection thread: ");
        bw.write(connectionThread == null ? "null" : connectionThread.toString());
        bw.newLine();
        bw.write(" - client threads:");
        bw.newLine();
        bw.write("  - count: ");
        bw.write(Integer.toString(clientThreads.size()));
        bw.newLine();
        for (Thread t : clientThreads) {
            bw.write("  - ");
            bw.write(t.toString());
            bw.newLine();
        }
        bw.write("--- SERVER STATUS ---");
        bw.newLine();
        bw.flush();
    }
