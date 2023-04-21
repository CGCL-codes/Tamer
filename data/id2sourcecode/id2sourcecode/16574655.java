    public static void main(String[] argv) {
        GenerateKeyPair generateKeyPair = null;
        try {
            if (argv.length == 1) {
                String file = argv[0];
                Properties defaults = new Properties();
                defaults.put("format", "openssh");
                defaults.put("algorithm", "RSA");
                defaults.put("bits", "1024");
                Properties definition = new Properties(defaults);
                definition.load(new FileInputStream(file));
                generateKeyPair = new GenerateKeyPair(definition);
            } else {
                generateKeyPair = new GenerateKeyPair("openssh", "RSA", 1024, "./id_rsa", null, null, "");
            }
            generateKeyPair.saveKeyPair();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't load file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }
