    public static AESUtil getInstance() {
        if (s_INSTANCE == null) {
            String seedname = System.getProperty("aes.seed");
            if (seedname == null) seedname = "/ld2c.txt";
            URL url = AESUtil.class.getResource(seedname);
            DataInputStream dis;
            String key = null;
            try {
                dis = new DataInputStream(url.openConnection().getInputStream());
                key = dis.readLine();
                dis.close();
            } catch (IOException e) {
                log.fatal("The file, ld2c.txt, but the encryption seed could not be read from that file.");
                System.exit(-1);
            } catch (Throwable e) {
                log.fatal("The seed required for encryption was not found. This encryption " + "seed should be in a file in the application working directory named ld2c.txt.");
                System.exit(-1);
            }
            s_INSTANCE = new AESUtil(key);
        }
        return s_INSTANCE;
    }
