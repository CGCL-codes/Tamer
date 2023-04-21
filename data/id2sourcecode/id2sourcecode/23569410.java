    private String doTranslate(String sourceText, String code) {
        String source = URLEncoder.encode(sourceText);
        String translated = null;
        try {
            URL url = new URL("http://babel.altavista.com/sites/babelfish/tr");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            StringBuffer postData = new StringBuffer();
            postData.append("doit=done&tt=urltext&urltext=");
            postData.append(source);
            postData.append("&lp=");
            postData.append(code);
            out.print(postData.toString());
            out.close();
            BufferedReader input = new BufferedReader(new InputStreamReader(new DataInputStream(connection.getInputStream()), "UTF-8"));
            translated = parseTranslation(input);
            input.close();
        } catch (Exception e) {
            System.out.println("Problem connecting to URL");
            System.out.println(e);
            e.printStackTrace();
        }
        return translated;
    }
