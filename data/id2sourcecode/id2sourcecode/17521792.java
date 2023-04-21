    public void handleRequest(String request, BufferedReader reader, BufferedWriter writer) throws IOException {
        writeOKHeader(writer);
        String[] values = request.split("&");
        String address = values[0];
        int port = Integer.parseInt(values[1]);
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            BufferedWriter swriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader sreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            swriter.write("MapTool\n");
            swriter.flush();
            String response = sreader.readLine();
            boolean success = "MapTool".equals(response);
            writer.write(Boolean.toString(success));
            System.out.println(new SimpleDateFormat().format(System.currentTimeMillis()) + " - " + address + " - " + port + ": " + success);
        } catch (Exception e) {
            System.out.println(new SimpleDateFormat().format(System.currentTimeMillis()) + " - " + address + " - " + port + ": " + e);
            writer.write("false");
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
