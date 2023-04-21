    private void processClientRequest(Socket clientSocket) throws IOException {
        InputStream input = clientSocket.getInputStream();
        OutputStream output = clientSocket.getOutputStream();
        long time = System.currentTimeMillis();
        output.write(("HTTP/1.1 200 OK\n\n<html><body>" + "Singlethreaded Server: " + time + "</body></html>").getBytes());
        output.close();
        input.close();
        System.out.println("Request processed:" + time);
    }
