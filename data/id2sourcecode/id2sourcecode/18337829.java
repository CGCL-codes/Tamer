    public void xlsLoader(String paramBusca, String caminhoArquivo) {
        try {
            path = caminhoArquivo;
            OutputStream out = new FileOutputStream(path + "/Cotacoes.txt", false);
            URL url = new URL("http://br.finance.yahoo.com/d/quotes.csv?s=" + paramBusca + "&f=sl1d1t1c1ohgv&e=.csv");
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            int i = 0;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
            in.close();
            out.close();
            System.out.println("XLS baixado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao baixar o xls!");
        }
    }
