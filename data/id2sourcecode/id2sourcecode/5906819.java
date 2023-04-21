    protected ResourceBundle getConfigFile() {
        File arquivo = new File("pshell.properties");
        if (!arquivo.exists()) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/pshell/base/default.properties");
                FileWriter outputStream = new FileWriter("pshell.properties");
                int c;
                while ((c = inputStream.read()) != -1) outputStream.write(c);
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Arquivo pshell.properties criado no diretorio " + System.getProperty("user.dir"));
        }
        try {
            ResourceBundle.clearCache();
            return ResourceBundle.getBundle("pshell");
        } catch (Exception e) {
        }
        return null;
    }
