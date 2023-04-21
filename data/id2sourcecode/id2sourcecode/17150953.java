    public static void checked2kPath() {
        File cpf = new File(WarriorAnt.ed2kPath);
        boolean created = false;
        if (cpf.exists() && !cpf.isDirectory()) {
            cpf.delete();
            cpf.mkdir();
            created = true;
        } else if (!cpf.exists()) {
            cpf.mkdir();
            created = true;
        }
        if (created) {
            URL url = (new Base16()).getClass().getClassLoader().getResource("anims/ed2k_1.gif");
            if (!(url == null)) {
                try {
                    InputStream is = url.openStream();
                    FileOutputStream fos = new FileOutputStream(new File(WarriorAnt.ed2kPath + "ed2k_1.gif"));
                    while (is.available() > 0) {
                        byte[] buff = new byte[500000];
                        int read = is.read(buff);
                        fos.write(buff, 0, read);
                    }
                    fos.close();
                } catch (Exception e) {
                    _logger.error("Error in copying picture to ed2k folder", e);
                }
            }
            url = (new Base16()).getClass().getClassLoader().getResource("anims/ed2k_2.gif");
            if (!(url == null)) {
                try {
                    InputStream is = url.openStream();
                    FileOutputStream fos = new FileOutputStream(new File(WarriorAnt.ed2kPath + "ed2k_2.gif"));
                    while (is.available() > 0) {
                        byte[] buff = new byte[500000];
                        int read = is.read(buff);
                        fos.write(buff, 0, read);
                    }
                    fos.close();
                } catch (Exception e) {
                    _logger.error("Error in copying picture to ed2k folder", e);
                }
            }
        }
    }
