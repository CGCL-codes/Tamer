    private Properties gerarCotacao(String nome) {
        Properties result = new Properties();
        try {
            String[] siglaNome = nome.split("-");
            URL url = new URL("http://br.finance.yahoo.com/d/quotes.csv?s=" + siglaNome[0] + "&f=sl1d1t1c1ohgv&e=.csv");
            URLConnection con = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine = null;
            inputLine = br.readLine();
            br.close();
            String s[] = null;
            if (inputLine.contains(",")) {
                s = inputLine.split(",");
            } else {
                s = inputLine.split(";");
            }
            result.setProperty("Sigla", siglaNome[0]);
            result.setProperty("Nome", siglaNome[1]);
            result.setProperty("Acao", s[0]);
            result.setProperty("Ultima transacao", s[1]);
            result.setProperty("Data", s[2]);
            result.setProperty("Hora", s[3]);
            if (s[4].charAt(0) == '+') {
                result.setProperty("Percentual", "<span style='color:blue'>" + s[4] + "</span>");
            } else if (s[4].charAt(0) == '-') {
                result.setProperty("Percentual", "<span style='color:red'>" + s[4] + "</span>");
            } else {
                result.setProperty("Percentual", s[4]);
            }
            result.setProperty("Valor de abertura", s[5]);
            result.setProperty("Máximo de venda", s[6]);
            result.setProperty("Minimo de venda", s[7]);
            result.setProperty("Volume", s[8]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
