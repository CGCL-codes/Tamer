    public String build(Map<String, String> params) {
        try {
            URL url = getClass().getClassLoader().getResource(templateFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            StringBuffer buff = new StringBuffer("");
            while ((line = reader.readLine()) != null) {
                buff.append(line);
            }
            reader.close();
            String htmlText = buff.toString();
            if (params != null) {
                for (String key : params.keySet()) {
                    String pattern = "\\$\\{" + key + "\\}";
                    htmlText = htmlText.replaceAll(pattern, params.get(key));
                }
            }
            return htmlText;
        } catch (IOException e) {
            throw new UnexpectedException("Error leyendo plantilla de mail", e);
        }
    }
