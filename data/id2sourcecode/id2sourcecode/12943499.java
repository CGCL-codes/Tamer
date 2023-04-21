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
