    public void ObtemArquivoTxt() {
        try {
            String userDir = System.getProperty("user.dir");
            File file = new File(userDir + "/build/web/WEB-INF/classes/teste/arquivosBaixados/Titulos_Negociaveis.txt");
            OutputStream out = new FileOutputStream(file, false);
            URL url = new URL("http://www.bovespa.com.br/suplemento/ExecutaAcaoDownload.asp?arquivo=Titulos_Negociaveis.txt");
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            int i = 0;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
            in.close();
            out.close();
            System.out.println("Download efetuado com sucesso");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado. Causa: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Erro na formatação da URL. Causa: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro de entrada/saida de dados. Causa: " + e.getMessage());
        }
    }
