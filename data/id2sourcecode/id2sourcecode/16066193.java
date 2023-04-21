    public void run() {
        try {
            for (int i = _numConnections; i > 0 || _numConnections == -1; i--) {
                Socket socket = _serverSocket.accept();
                System.out.println("server: accepting connection");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = in.readLine();
                while (line != null && line.length() > 0) {
                    System.out.println(line);
                    line = in.readLine();
                }
                System.out.println("server: read request");
                InputStream stream = getStream();
                String response = "HTTP/1.0 200 OK\r\n" + "Content-Type: " + "application/octet-stream\r\n" + "\r\n";
                OutputStream out = socket.getOutputStream();
                out.write(response.getBytes());
                System.out.println("server: sent response header");
                System.out.println(response);
                int bytes_read;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytes_read = stream.read(buffer)) > 0) {
                    out.write(buffer, 0, bytes_read);
                    System.out.println("server: wrote " + bytes_read + " bytes");
                }
                out.flush();
                stream.close();
                System.out.println("server: closing connection");
                socket.close();
            }
        } catch (SocketTimeoutException to_ex) {
            System.out.println("server timed out");
            try {
                System.out.println("server: exiting");
                _serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
