    public static String getBarraTitulo() {
        String barraNavegacao = "http://www.caixa.gov.br";
        HttpURLConnection connection;
        try {
            URL url = new URL(barraNavegacao);
            connection = (HttpURLConnection) (url.openConnection());
            connection.getInputStream();
            return barraNavegacao;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return barraNavegacao;
    }
