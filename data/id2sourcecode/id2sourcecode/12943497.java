    public static void spawn(int port, final String name) {
        try {
            final ServerSocket server = new ServerSocket(port);
            GroupSender gp = new GroupSender("textmash", 5000) {

                @Override
                public void send(OutputStream output) throws Exception {
                    DataOutputStream dat = new DataOutputStream(output);
                    dat.writeUTF(name);
                    dat.writeInt(server.getLocalPort());
                    dat.flush();
                }
            };
            gp.fire();
            Socket cl;
            boolean running = true;
            while ((running) && (cl = server.accept()) != null) {
                final Socket client = cl;
                new Daemon() {

                    @Override
                    public void run() {
                        try {
                            DataInputStream reader = new DataInputStream(client.getInputStream());
                            int msg;
                            try {
                                for (; ; ) {
                                    msg = reader.readInt();
                                    if (msg == RUN_PROCESS) {
                                        String workingDir = reader.readUTF();
                                        String[] cmds = new String[reader.readInt()];
                                        for (int i = 0; i < cmds.length; ++i) {
                                            cmds[i] = reader.readUTF();
                                        }
                                        if (repl != null) {
                                            repl.destroy();
                                        }
                                        repl = new Process(new File(workingDir), cmds);
                                        Stream.redirect(repl.getInput(), System.out);
                                    } else if (repl != null) {
                                        if (msg == PUT_INPUT) {
                                            repl.getOutput().write(reader.readUTF().getBytes());
                                            repl.getOutput().flush();
                                        } else if (msg == DESTROY_PROCESS) {
                                            repl.destroy();
                                        }
                                    }
                                }
                            } catch (EOFException e) {
                            }
                        } catch (IOException e) {
                        }
                    }
                };
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
