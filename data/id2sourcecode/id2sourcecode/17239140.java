    public void run() {
        try {
            String msg;
            String[] array;
            while (!timeToClose) {
                msg = reader.readLine();
                if (msg.startsWith("SAY")) {
                    array = msg.split(" ", 2);
                    state.addMsg(connection.getContact(), array[1]);
                } else if (msg.equals("CHAT")) {
                    array = msg.split(" ", 3);
                    String id = array[1];
                    String message = array[2];
                    Contact c = state.getContactById(id);
                    state.addMsg(c, message);
                } else if (msg.equals("HELLO")) {
                    connection.getWriter().write("HELLO\n");
                    connection.getWriter().flush();
                } else if (msg.equals("BYE")) {
                    System.out.println("EXIT: " + connection.getContact().getNick());
                    close();
                    state.userHasExit(connection.getContact());
                } else if (msg.startsWith("CONTACTINFO")) {
                    array = msg.split(" ", 2)[1].split(";", 4);
                    System.out.println(msg);
                    String id = array[0];
                    String ip = array[1];
                    String nick = array[2];
                    String status = array[3];
                    Contact contact = new Contact(id, ip, nick, status);
                    if (!status.equals("OFFLINE")) {
                        Socket socket = new Socket(ip, Constants.CLIENT_PORT);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                        writer.write("CLIENTINFO " + state.getID() + ";" + state.getIP() + ";" + state.getNick() + ";" + state.getStatus());
                        writer.flush();
                        state.addConnection(socket, contact, reader, writer);
                        Thread reciever = new Thread(new Reciever(state, state.getConnection(contact)));
                        reciever.start();
                    }
                    state.getMainWindow().getContactTree().insert(contact);
                } else if (msg.startsWith("APPROVECLIENT")) {
                    array = msg.split(" ");
                    String tmp = array[1] + "\nhas added you to his/her list,\nwill you accept and put him on your list?";
                    if (JOptionPane.showConfirmDialog(null, tmp, "You have been added", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION) {
                        state.getServerConnection().getWriter().write("APPROVECLIENT " + array[1] + "\n");
                        state.getServerConnection().getWriter().flush();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Reciever1: " + e);
        }
    }
