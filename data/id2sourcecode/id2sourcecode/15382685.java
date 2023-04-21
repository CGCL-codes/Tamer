    public static String obterConteudoSite(String u, Map<String, String> parametros, Map<String, String> headers) {
        URL url;
        try {
            StringBuilder strParams = new StringBuilder();
            if (parametros != null) {
                for (String chave : parametros.keySet()) {
                    strParams.append(URLEncoder.encode(chave, "UTF-8"));
                    strParams.append("=");
                    strParams.append(URLEncoder.encode(parametros.get(chave), "UTF-8"));
                    strParams.append("&");
                }
            }
            url = new URL(u);
            URLConnection conn = null;
            if (proxy != null) conn = url.openConnection(proxy.getProxy()); else conn = url.openConnection();
            if (headers != null) {
                for (String header : headers.keySet()) {
                    conn.setRequestProperty(header, headers.get(header));
                }
            }
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(strParams.toString());
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
            String line;
            StringBuilder resultado = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                resultado.append(line);
            }
            wr.close();
            rd.close();
            return resultado.toString();
        } catch (MalformedURLException e) {
            throw new AlfredException("Não foi possível obter contato com o site " + u, e);
        } catch (IOException e) {
            throw new AlfredException("Não foi possível obter contato com o site " + u, e);
        }
    }
