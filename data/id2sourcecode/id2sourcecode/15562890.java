    private String getPage(String sourceAdresse) throws java.sql.SQLException {
        String toreturn = null;
        try {
            URL url = new URL(sourceAdresse);
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            int c = in.read();
            StringBuilder build = new StringBuilder();
            while (c != -1) {
                build.append((char) c);
                c = in.read();
            }
            toreturn = build.toString();
        } catch (Exception e) {
            throw new java.sql.SQLException("Erreur d'adresse url : La page '" + this.dbwLocationIndexSimple + "' n'existe pas");
        }
        return toreturn;
    }
