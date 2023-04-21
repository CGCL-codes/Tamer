        public void run() {
            try {
                MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                DataInputStream in = new DataInputStream(socket.getLocalInputStream());
                OutputStream out = new DataOutputStream(socket.getLocalOutputStream());
                byte[] randomOut = new byte[128];
                rnd.nextBytes(randomOut);
                out.write(randomOut);
                out.flush();
                byte[] randomIn = new byte[128];
                in.readFully(randomIn);
                sha1.reset();
                byte[] digestOut = sha1.digest(randomOut);
                sha1.reset();
                byte[] digestIn = sha1.digest(randomIn);
                out.write(digestOut);
                out.flush();
                byte[] digestInReal = new byte[digestIn.length];
                in.readFully(digestInReal);
                if (!Arrays.equals(digestIn, digestInReal)) {
                    System.out.println("Watchdog digest does not match!");
                    if (dog.timeout != 0) dog.setNextTimeout(0);
                } else if (dog.timeout != 0) {
                    dog.setNextTimeout(System.currentTimeMillis() + dog.timeout);
                }
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                if (dog.timeout != 0) dog.setNextTimeout(0);
            }
        }
