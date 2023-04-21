        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                byte[] challenge = new byte[512];
                R.nextBytes(challenge);
                out.writeInt(220);
                out.writeObject(challenge);
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(password);
                md.update(challenge);
                byte[] response = (byte[]) in.readObject();
                if (Arrays.equals(response, md.digest())) {
                    out.writeObject(debuggerStub);
                } else {
                    out.writeObject(null);
                }
            } catch (Exception e) {
                logger.warn("Connection to " + s.getInetAddress().getHostAddress() + " abruply broke", e);
            }
        }
