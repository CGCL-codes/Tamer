    public String submit(String action, HashMap<String, String> dado) throws Exception {
        StringBuilder stream = new StringBuilder();
        URL url = new URL(action);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("POST");
        conexao.setDoOutput(true);
        conexao.setDoInput(true);
        DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
        Set chaves = dado.keySet();
        Iterator chaveIterador = chaves.iterator();
        String conteudo = "";
        for (int i = 0; chaveIterador.hasNext(); i++) {
            Object key = chaveIterador.next();
            if (i != 0) {
                conteudo += "&";
            }
            conteudo += key + "=" + URLEncoder.encode(dado.get(key), "UTF-8");
        }
        saida.writeBytes(conteudo);
        saida.flush();
        saida.close();
        InputStream entrada = conexao.getInputStream();
        byte[] buffer = new byte[1024];
        while (entrada.read(buffer) != -1) {
            stream.append(new String(buffer));
        }
        entrada.close();
        return stream.toString();
    }
