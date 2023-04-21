    private String listArquivo() {
        String arquivo = "";
        String linha = "";
        try {
            URL url = new URL(appletex.getCodeBase(), "./listador?dir=" + "arquivos/" + JLinkitFrame.user);
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            InputStream in = con.getInputStream();
            DataInputStream result = new DataInputStream(new BufferedInputStream(in));
            while ((linha = result.readLine()) != null) {
                arquivo += linha + "\n";
            }
            return arquivo;
        } catch (Exception e) {
            return null;
        }
    }
