    public InputStream getArquivoS(String nomeArq) {
        try {
            URL url = new URL(getCodeBase(), "./abridor?arq=" + user + "/" + nomeArq);
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            InputStream in = con.getInputStream();
            return in;
        } catch (Exception e) {
            return null;
        }
    }
