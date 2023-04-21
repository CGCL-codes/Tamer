    public void initConf() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            conf = new Loggerconf();
            System.out.print("Logger port: ");
            conf.setPort(Integer.parseInt(in.readLine()));
            System.out.println("Kerberos server address: ");
            conf.setKerberos(in.readLine());
            System.out.print("Logger service principal(with @ suffix): ");
            conf.setServiceName(in.readLine());
            System.out.print("Logger service password: ");
            conf.setPassword(in.readLine());
            System.out.print("Strongbox server principal(with @ suffix): ");
            conf.setStrongboxPrinc(in.readLine());
            conf.saveToFile(CONF_FILENAME);
            (new File("./logs")).mkdir();
            BufferedReader inConf = new BufferedReader(new InputStreamReader(StrongboxLogger.class.getResourceAsStream("/LoggerServer.config")));
            PrintWriter outConf = new PrintWriter(new FileOutputStream("LoggerServer.config"), true);
            String read = null;
            while ((read = inConf.readLine()) != null) outConf.write(read + "\n");
            inConf.close();
            outConf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
