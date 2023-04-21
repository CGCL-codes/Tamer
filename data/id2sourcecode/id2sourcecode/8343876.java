    public void start_server(boolean catch_errors_p, boolean backtrace_p) {
        if ((port < 1024) || port >= 65536) {
            throw new RuntimeException("Port number " + port + " out of range.");
        }
        while (true) {
            ServerSocket server_socket = null;
            try {
                try {
                    server_socket = new ServerSocket(port);
                } catch (Exception e1) {
                    throw new RuntimeException(e1.toString());
                }
                Connection conn = LocalConnection.local_connection();
                try {
                    conn.register_interest_in_side_effects(this);
                    while (true) {
                        Socket socket = null;
                        try {
                            socket = server_socket.accept();
                        } catch (Exception e2) {
                            throw new ConnectionProblem(e2.toString());
                        }
                        PrintWriter write_stream = null;
                        try {
                            write_stream = new PrintWriter(socket.getOutputStream());
                        } catch (IOException e3) {
                            throw new ConnectionProblem(e3.toString());
                        }
                        BufferedReader read_stream = null;
                        try {
                            read_stream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        } catch (IOException e4) {
                            throw new ConnectionProblem(e4.toString());
                        }
                        if ((read_stream != null) && (write_stream != null)) {
                            handle_connection(server_socket, read_stream, write_stream, catch_errors_p, backtrace_p);
                        }
                        try {
                            socket.close();
                        } catch (IOException e5) {
                            throw new ConnectionProblem(e5.toString());
                        }
                    }
                } finally {
                    conn.register_interest_in_side_effects(this);
                }
            } catch (ConnectionProblem problem) {
                System.out.println("Server connection problem:  " + problem);
            }
        }
    }
