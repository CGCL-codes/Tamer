    public String sendMessage(String a_message) {
        String response = "";
        try {
            Socket socket = new Socket(my_serverHost, my_serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(a_message + "\nEND");
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket.getChannel();
            response = in.readLine();
            out.close();
            in.close();
            socket.close();
        } catch (IOException ioe) {
        } catch (SecurityException se) {
            System.err.println("Security Exception");
        }
        return response;
    }
