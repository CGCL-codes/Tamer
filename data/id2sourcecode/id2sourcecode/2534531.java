    public ResultSet executeQuery(String query) throws SQLException {
        ResultSet result = new ResultSet();
        query = query.trim();
        String url = HOST + translateQuery(query) + "&cs=" + createCheckSum(query);
        if (query.startsWith("SELECT") || query.startsWith("select")) {
            int fromIndex = query.indexOf("FROM") > 0 ? query.indexOf("FROM") : query.indexOf("from");
            String[] kolumny = query.substring("SELECT".length(), fromIndex).split(",");
            for (int i = 0; i < kolumny.length; i++) {
                kolumny[i] = kolumny[i].trim();
                if (kolumny[i].contains(" AS ")) {
                    kolumny[i] = kolumny[i].split(" ")[2];
                }
            }
            result.setColumnNames(new ArrayList<String>(Arrays.asList(kolumny)));
        }
        try {
            BufferedReader we = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()));
            String wiersz;
            int end;
            int start;
            while ((wiersz = we.readLine()) != null) {
                if (wiersz.contains("<table border=0><tr><td id='kodStanu'>")) {
                    start = "<table border=0><tr><td id='kodStanu'>".length();
                    end = wiersz.indexOf("</td></tr>");
                    setState(Short.valueOf(wiersz.substring(start, end)).shortValue());
                    if (!checkResultCount()) {
                        return null;
                    }
                    wiersz = we.readLine();
                    if (wiersz.contains("<tr><td id='iloscWynikow'>")) {
                        start = "<tr><td id='iloscWynikow'>".length();
                        end = wiersz.indexOf("</td></tr>");
                        setResultCount(Short.valueOf(wiersz.substring(start, end)).shortValue());
                    }
                }
                if (wiersz.contains("<tr><td>")) {
                    ArrayList<Object> row = new ArrayList<Object>();
                    while (wiersz.contains("<td>")) {
                        start = wiersz.indexOf("<td>") + "<td>".length();
                        end = wiersz.indexOf("</td>");
                        try {
                            row.add(wiersz.substring(start, end));
                        } catch (Exception e) {
                            return result;
                        }
                        wiersz = wiersz.substring(end + 1);
                    }
                    result.addRow(row);
                }
            }
            return result;
        } catch (MalformedURLException e) {
            System.err.println("Blad tworzenia adresu URL!Sprawdz klase KnipJDBC:19\n" + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Blad strumienia I/O!Sprawdz klase KnipJDBC:19\n" + e.getMessage());
            return null;
        } catch (URISyntaxException e) {
            System.err.println("Blad tłumaczenia URL!Sprawdz klase KnipJDBC:19\n" + e.getMessage());
            return null;
        }
    }
