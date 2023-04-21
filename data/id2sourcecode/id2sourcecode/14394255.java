    public void run() {
        byte[] bytesArray = null;
        try {
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
            } catch (ConnectException e) {
                System.err.println("[!] Can't connect to " + url.toString());
                return;
            } catch (IOException e) {
                System.err.println("[!] HttpUnit : " + e.getMessage());
                return;
            }
            Vector<Byte> datas = new Vector<Byte>();
            DataInputStream input;
            try {
                input = new DataInputStream(new BufferedInputStream(conn.getInputStream()));
            } catch (IOException e) {
                System.err.println("[!] HttpUnit " + url.toString() + ") not found");
                return;
            }
            try {
                while (input.available() != 0) {
                    datas.add(new Byte((byte) input.readByte()));
                }
                bytesArray = new byte[datas.size()];
                int i;
                for (i = 0; i < datas.size(); i++) {
                    bytesArray[i] = datas.elementAt(i).byteValue();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                manager.addRequestResults(url.toString(), bytesArray);
            } catch (NullPointerException e) {
            }
            manager.requestFinalize();
        }
    }
