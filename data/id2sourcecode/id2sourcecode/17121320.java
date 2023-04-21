        public void run() {
            InputStream is = null;
            OutputStream os = null;
            int in, out;
            boolean finished = false;
            try {
                is = sock.getInputStream();
                os = sock.getOutputStream();
                while (!finished) {
                    System.out.println("[SERVER] threadID " + getId() + " reading ...");
                    in = is.read();
                    if (in == -1) finished = true; else {
                        System.out.println("[SERVER] threadID " + getId() + " reads " + (char) in);
                        out = in - '0' + 'a';
                        System.out.println("[SERVER] threadID " + getId() + " writes " + (char) out);
                        os.write(out);
                    }
                }
            } catch (IOException e) {
            }
            try {
                assert (is != null && os != null && sock != null);
                is.close();
                os.close();
                sock.close();
            } catch (IOException e) {
            }
            System.out.println("[AlphabetServer.WorkerThread : run() threadID=" + getId() + "] end of stream");
        }
