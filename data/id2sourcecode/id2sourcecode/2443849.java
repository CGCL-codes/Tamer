    private void processRequests() throws Exception {
        int transType = ASCII;
        int structure = FILE;
        boolean running = true;
        output.write("220 Service ready".getBytes());
        output.write(CRLF.getBytes());
        while (running) {
            String requestLine = br.readLine();
            System.out.println(requestLine);
            StringTokenizer s = new StringTokenizer(requestLine);
            String temp = s.nextToken();
            boolean cmd_supported = false;
            String fileName;
            String serverLine = "Server: jayshare FTP server" + CRLF;
            String answer = null;
            FileInputStream fis = null;
            boolean fileExists = false;
            if (temp.equals("USER")) {
                cmd_supported = true;
                answer = "331 User name ok, user logged in";
            } else if (temp.equals("PASS")) {
                cmd_supported = true;
                answer = "230 User logged in";
            } else if (temp.equals("PORT")) {
                cmd_supported = true;
                int ip1, ip2, ip3, ip4;
                ip1 = Integer.valueOf(s.nextToken(", ")).intValue();
                ip2 = Integer.valueOf(s.nextToken()).intValue();
                ip3 = Integer.valueOf(s.nextToken()).intValue();
                ip4 = Integer.valueOf(s.nextToken()).intValue();
                port = 256 * Integer.valueOf(s.nextToken()).intValue();
                port += Integer.valueOf(s.nextToken()).intValue();
                addr = InetAddress.getByName(ip1 + "." + ip2 + "." + ip3 + "." + ip4);
                answer = "200 Command OK";
            } else if (temp.equals("TYPE")) {
                cmd_supported = true;
                String type = s.nextToken();
                if (type.equals("I")) {
                    transType = BINARY;
                    answer = "200 Command OK";
                } else if (type.equals("A")) {
                    transType = ASCII;
                    answer = "200 Command OK";
                } else answer = "504 Command not implemented for that parameter" + CRLF + "221- only (I)mage and (A)SCII are supported.";
            } else if (temp.equals("MODE")) {
                cmd_supported = true;
            } else if (temp.equals("STRU")) {
                cmd_supported = true;
                String stru = s.nextToken();
                if (stru.equals("F")) structure = FILE; else if (stru.equals("R")) structure = RECORD; else answer = "504 Command not implemented for that parameter" + CRLF + "221- only (F)ile and (R)ecord are supported.";
            } else if (temp.equals("RETR")) {
                cmd_supported = true;
            } else if (temp.equals("STOR")) {
                cmd_supported = true;
            } else if (temp.equals("NOOP")) {
                cmd_supported = true;
            } else if (temp.equals("PASV")) {
                cmd_supported = true;
                answer = "Entering Passive Mode (" + serverAddr.getHostAddress() + "," + 232 + "," + 222 + ")";
            } else if (temp.equals("QUIT")) {
                cmd_supported = true;
                running = false;
            }
            if (cmd_supported && answer != null) {
                output.write(answer.getBytes());
            } else output.write("502 Command not implemented".getBytes());
            output.write(CRLF.getBytes());
        }
        try {
            output.close();
            br.close();
            socket.close();
        } catch (Exception e) {
        }
    }
