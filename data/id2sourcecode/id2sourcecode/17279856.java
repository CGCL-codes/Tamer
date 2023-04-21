    public static void main(String[] args) throws ConfigurationException, FileNotFoundException, IOException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KP_ALGO);
        kpg.initialize(KP_ALGO_STR);
        String domainName = "agora.ece.cmu.edu";
        String domainKPFile = String.format("domain.kp");
        {
            KeyPair domainKP = kpg.generateKeyPair();
            ObjectOutputStream kpos = new ObjectOutputStream(new FileOutputStream(domainKPFile));
            kpos.writeObject(domainKP);
            kpos.close();
        }
        for (int i = NODE_START; i <= NODE_END; i++) {
            PropertiesConfiguration pconf = new PropertiesConfiguration();
            DataConfiguration dconf = new DataConfiguration(pconf);
            dconf.setProperty("domain.name", domainName);
            dconf.setProperty("domain.kp", "conf/" + domainKPFile);
            KeyPair kp = kpg.generateKeyPair();
            String kpfile = String.format("%d.kp", i);
            ObjectOutputStream kpos = new ObjectOutputStream(new FileOutputStream(kpfile));
            kpos.writeObject(kp);
            kpos.close();
            dconf.setProperty("local.kp", "conf/" + kpfile);
            dconf.setProperty("local.host", String.format(HOST_FORMAT, i));
            dconf.setProperty("local.port", PORT);
            if (i != NODE_BOOT) {
                int bsNode = (int) (Math.random() * (i - NODE_START)) + NODE_START;
                dconf.setProperty("remote.host", String.format(HOST_FORMAT, bsNode));
                dconf.setProperty("remote.port", PORT);
            }
            dconf.setProperty("behavior", "exit");
            pconf.save(String.format("%d.conf", i));
        }
    }
