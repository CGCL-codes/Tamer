    public static Collection<Map<String, String>> interpretar(String u) {
        URL url;
        Collection<Map<String, String>> c = new ArrayList<Map<String, String>>();
        try {
            url = new URL(u);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            boolean primeiraLinha = true;
            String[] keys = null;
            while ((line = rd.readLine()) != null) {
                if (line.trim().charAt(0) == '#') continue;
                if (line.trim().split(",").length == 0) continue;
                if (primeiraLinha) {
                    primeiraLinha = false;
                    CSV csv = new CSV();
                    csv.split(line);
                    keys = new String[csv.getNField() - 1];
                    for (int i = 0; i < csv.getNField() - 1; i++) keys[i] = csv.getField(i);
                } else {
                    CSV csv = new CSV();
                    Map<String, String> retorno = new HashMap<String, String>();
                    csv.split(line);
                    for (int i = 0; i < csv.getNField() - 1; i++) {
                        retorno.put(keys[i], csv.getField(i));
                    }
                    c.add(retorno);
                }
            }
            rd.close();
            return c;
        } catch (MalformedURLException e) {
            throw new AlfredException("N�o foi poss�vel obter contato com o site " + u, e);
        } catch (IOException e) {
            throw new AlfredException("N�o foi poss�vel obter contato com o site " + u, e);
        }
    }
