    private void sendFile(HTTPrequest request, InputStream inFile) {
        PrintStream outbound = null;
        try {
            outbound = new PrintStream(request.clientSocket.getOutputStream(), true);
            outbound.print("HTTP/1.0 200 OK\r\n");
            outbound.print("Content-type: text/html\r\n");
            outbound.print("Content-Length: " + inFile.available() + "\r\n");
            outbound.print("\r\n");
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
            if (!request.method.equals("HEAD")) {
                byte[] dataBody = new byte[1024];
                while (inFile.read(dataBody) != -1) outbound.write(dataBody);
            }
            outbound.close();
            request.inbound.close();
        } catch (IOException ioe) {
            Debug.output(3, ioe);
        }
    }
