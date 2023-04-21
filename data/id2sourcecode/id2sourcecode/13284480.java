    private void handleCrossDomain() throws IOException, URISyntaxException {
        out.println("HTTP/0.9 200 OK");
        out.println("Server: RoomWare Admin Communicator");
        out.println("Content-Type: text/html");
        out.println("Connection: close");
        out.println("");
        Scanner ins = new Scanner(getClass().getResourceAsStream("/docs/crossdomain.xml"));
        while (ins.hasNextLine()) {
            out.println(ins.nextLine());
        }
    }
