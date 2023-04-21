    @Override
    public void run() {
        try {
            URL url = new URL(URL_EXPORT_ACTION);
            getLabel().setText("Estabelecendo conex�o a mobileformeditor.appspot.com ...");
            StringBuilder menssage = new StringBuilder("nick=JV&senha=1234&arquivo=");
            menssage = menssage.append(getXml());
            System.out.println("Mensagem a Enviar: " + menssage);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            System.out.println(writer.getEncoding());
            getLabel().setText("Transmitindo ...");
            writer.write(new String(menssage.toString().getBytes("ISO-8859-1")));
            writer.flush();
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
            System.out.println(answer.toString());
            if (answer.toString().compareTo("OK") == 0) {
                getLabel().setText("Opera��o conclu�da com sucesso!");
            } else {
                getLabel().setText("Erro na transmiss�o: " + answer.toString());
            }
        } catch (MalformedURLException e) {
            getLabel().setText("Erro na transmiss�o: URL");
        } catch (IOException e) {
            getLabel().setText("Erro na transmiss�o: IO");
        }
    }
