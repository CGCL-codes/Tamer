    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage: java ../.. aie.server.cli " + "<ip of server> <port> [-debug]");
        } else {
            boolean debug_enabled = false;
            if (args.length == 5 && args[4].equalsIgnoreCase("-debug")) {
                debug_enabled = true;
            }
            try {
                Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
                DataInputStream in = new DataInputStream(socket.getInputStream());
                int port = in.readInt();
                socket.close();
                socket = new Socket(args[0], port);
                in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                BufferedReader clit = new BufferedReader(new InputStreamReader(System.in));
                out.write(aie.server.Authenticator.mgcnum);
                out.write(protocol);
                byte[] b;
                b = new byte[aie.server.Authenticator.cmd_accept.length];
                in.read(b);
                if (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_accept)) {
                    System.out.println("Magic number not accepted.");
                    System.exit(1);
                }
                if (debug_enabled) {
                    System.out.println("Magic number accepted.");
                }
                b = new byte[aie.server.Authenticator.srvr_wlcm.length];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.srvr_wlcm)) {
                    in.read(b);
                }
                b = new byte[in.readInt()];
                in.read(b);
                System.out.println(new String(b));
                b = new byte[1];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_user)) {
                    in.read(b);
                }
                System.out.print("Enter your username (\"new\" to create" + " a new account): ");
                String username = clit.readLine();
                boolean create = false;
                if (username.equalsIgnoreCase("new")) {
                    create = true;
                    out.write(aie.server.Authenticator.cmd_signup);
                    b = new byte[1];
                    in.read(b);
                    while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_user)) {
                        in.read(b);
                    }
                    System.out.print("username: ");
                    username = clit.readLine();
                }
                out.write(aie.server.Authenticator.cmd_user);
                b = username.getBytes();
                out.writeInt(b.length);
                out.write(b);
                b = new byte[1];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_pass)) {
                    in.read(b);
                }
                System.out.print("Enter your password: ");
                String password = clit.readLine();
                if (create) {
                    System.out.print("Reenter your password: ");
                    String tmp = clit.readLine();
                    while (!tmp.equals(password)) {
                        System.out.println("Passwords do not match.");
                        System.out.print("pasword: ");
                        password = clit.readLine();
                        System.out.print("again: ");
                        tmp = clit.readLine();
                    }
                }
                out.write(aie.server.Authenticator.cmd_pass);
                b = password.getBytes();
                out.writeInt(b.length);
                out.write(b);
                if (create) {
                    b = new byte[1];
                    in.read(b);
                    while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_race)) {
                        in.read(b);
                    }
                    System.out.println("Choose your race:");
                    int num = in.readInt();
                    for (int i = 0; i < num; i++) {
                        b = new byte[in.readInt()];
                        in.read(b);
                        System.out.println(i + " " + new String(b));
                    }
                    System.out.print(":");
                    int choice = -1;
                    while (choice == -1) {
                        String tmp = clit.readLine();
                        try {
                            choice = Integer.parseInt(tmp);
                            if (choice < 0 || choice >= num) {
                                choice = -1;
                                System.out.print("Invalid choice, try again:");
                            }
                        } catch (NumberFormatException nfe) {
                            System.out.print("Invalid choice, try again:");
                        }
                    }
                    out.write(aie.server.Authenticator.cmd_race);
                    out.writeInt(choice);
                    b = new byte[1];
                    in.read(b);
                    while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_class)) {
                        in.read(b);
                    }
                    System.out.println("Choose your class:");
                    num = in.readInt();
                    for (int i = 0; i < num; i++) {
                        b = new byte[in.readInt()];
                        in.read(b);
                        System.out.println(i + " " + new String(b));
                    }
                    System.out.print(":");
                    choice = -1;
                    while (choice == -1) {
                        String tmp = clit.readLine();
                        try {
                            choice = Integer.parseInt(tmp);
                            if (choice < 0 || choice >= num) {
                                choice = -1;
                                System.out.print("Invalid choice, try again:");
                            }
                        } catch (NumberFormatException nfe) {
                            System.out.print("Invalid choice, try again:");
                        }
                    }
                    out.write(aie.server.Authenticator.cmd_class);
                    out.writeInt(choice);
                }
                System.out.println("waiting for UPDCHK");
                b = new byte[1];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.srvr_updchk)) {
                    in.read(b);
                }
                System.out.println("recieved updchk, sending info");
                out.writeInt(1);
                out.write(sid);
                out.write(ver);
                System.out.println("waiting for updates");
                for (int i = 0; i < in.readInt(); i++) {
                    b = new byte[2];
                    if (aie.server.Authenticator.verify(b, sid)) {
                        for (long j = 0; j < in.readLong(); j++) {
                        }
                    }
                }
                System.out.println("all updates recieved successfully");
                out.write(aie.server.Authenticator.cmd_ready);
                System.out.println("READY sent, awaiting NFO");
                b = new byte[aie.server.Authenticator.cmd_nfo.length];
                in.read(b);
                while (!aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_nfo)) {
                    in.read(b);
                }
                System.out.println("NFO recieved, sending READY");
                out.write(aie.server.Authenticator.cmd_ready);
                System.out.println("connected!");
                getRoomInfo(in);
                String cmd;
                System.out.print("> ");
                cmd = clit.readLine().toLowerCase();
                boolean goodcmd = true;
                while (!cmd.equals("quit") && !cmd.equals("logoff") && !cmd.equals("exit") && !cmd.equals("done")) {
                    goodcmd = true;
                    if (cmd.equals("n")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.N);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("e")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.E);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("s")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.S);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("w")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.W);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("ne")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.NE);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("se")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.SE);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("sw")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.SW);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("nw")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.NW);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("u")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.U);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("d")) {
                        out.write(aie.server.Authenticator.cmd_move);
                        out.writeByte(Room.D);
                        out.writeInt(1);
                        cmd = "look";
                    } else if (cmd.equals("look")) {
                        in.readInt();
                        getRoomInfo(in);
                    } else if (cmd.startsWith("get")) {
                    } else {
                        System.out.println("unrecognized command.");
                        goodcmd = false;
                    }
                    if (goodcmd) {
                        b = new byte[1];
                        in.read(b);
                        if (aie.server.Authenticator.verify(b, aie.server.Authenticator.cmd_say)) {
                            int num = in.readInt();
                            for (int i = 0; i < num; i++) {
                                b = new byte[in.readInt()];
                                in.read(b);
                                StringTokenizer st = new StringTokenizer(new String(b));
                                System.out.print(st.nextToken() + ": " + st.nextToken());
                            }
                        }
                    }
                    System.out.print("> ");
                    cmd = clit.readLine();
                }
                in.close();
                out.close();
                clit.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
