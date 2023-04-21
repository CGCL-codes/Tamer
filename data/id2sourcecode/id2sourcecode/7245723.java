    private String listaCenarios() {
        String arquivo = "";
        String linha = "";
        try {
            URL url = new URL(this.getCodeBase(), "./listador?dir=" + "arquivos/cenarios/");
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            InputStream in = con.getInputStream();
            DataInputStream result = new DataInputStream(new BufferedInputStream(in));
            while ((linha = result.readLine()) != null) {
                arquivo += linha + "\n";
            }
            return arquivo;
        } catch (Exception e) {
            try {
                URL url = new URL(this.getCodeBase(), "./cenarios/");
                URLConnection con = url.openConnection();
                con.setUseCaches(false);
                InputStream in = con.getInputStream();
                DataInputStream result = new DataInputStream(new BufferedInputStream(in));
                while ((linha = result.readLine()) != null) {
                    arquivo += linha + "\n";
                }
                return arquivo;
            } catch (Exception ex) {
                return "";
            }
        }
    }
