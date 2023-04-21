        @Override
        public void run() {
            BufferedReader stdin = new BufferedReader(new InputStreamReader(Channels.newInputStream((new FileInputStream(FileDescriptor.in)).getChannel())));
            try {
                String line;
                while (keepRun) {
                    line = stdin.readLine();
                    if (sessionListeners) {
                        Iterator it = sessionTokens.keySet().iterator();
                        while (it.hasNext()) {
                            Value v = Value.create();
                            v.getFirstChild("token").setValue(it.next());
                            v.setValue(line);
                            sendMessage(CommMessage.createRequest("in", "/", v));
                        }
                    } else {
                        sendMessage(CommMessage.createRequest("in", "/", Value.create(line)));
                    }
                }
            } catch (ClosedByInterruptException ce) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
