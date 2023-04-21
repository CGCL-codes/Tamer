    public void generateKey(boolean useCache) throws NoSuchAlgorithmException {
        File file = new File(path);
        if (useCache && !needKeyUpdate()) {
            System.out.println("RSA key location: " + path);
            System.out.println("Loading RSA key");
            FileInputStream input;
            try {
                input = new FileInputStream(path);
                byte[] bytInput = new byte[(int) file.length()];
                input.read(bytInput);
                input.close();
                loadKey(bytInput);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        pairgen.initialize(KEYSIZE, random);
        keyPair = pairgen.generateKeyPair();
        if (!path.equals("")) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(path);
                fos.write(toByteArray(true));
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
