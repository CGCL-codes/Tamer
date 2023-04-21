    private ResultSet calculate(String query) {
        ResultSet result = new ResultSet();
        String url = HOST + crypt(query);
        try {
            BufferedReader we = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()));
            String wiersz;
            int end;
            int start;
            while ((wiersz = we.readLine()) != null) {
                if (wiersz.contains("<table border=0><tr><td id='kodStanu'>")) {
                    start = "<table border=0><tr><td id='kodStanu'>".length();
                    end = wiersz.indexOf("</td></tr>");
                    setState(Byte.valueOf(wiersz.substring(start, end)).byteValue());
                    wiersz = we.readLine();
                    if (wiersz.contains("<tr><td id='iloscWynikow'>")) {
                        start = "<tr><td id='iloscWynikow'>".length();
                        end = wiersz.indexOf("</td></tr>");
                        short iloscWynikow = Short.valueOf(wiersz.substring(start, end)).shortValue();
                        setResultCount(Long.valueOf(wiersz.substring(start, end)).longValue());
                        if (iloscWynikow == 0) {
                            warning.setNextWarning(new SQLWarning("[WARN] Zapytanienie nie zwróciło żadnych danych!"));
                            System.out.println("[WARN] Zapytanienie nie zwróciło żadnych danych!");
                        } else {
                            System.out.println("[WARN] Zapytanie zwróciło: " + iloscWynikow + " rekordów.");
                        }
                    }
                    if (wiersz.contains("<tr id='nazwyKolumn'>")) {
                        start = "<tr id='nazwyKolumn'>".length();
                        end = wiersz.lastIndexOf("</tr>");
                        while (wiersz.contains("<td>")) {
                            int pstart = wiersz.indexOf("<td>");
                            int pend = wiersz.indexOf("</td>");
                            this.columnNames.add(wiersz.substring(pstart + "<td>".length(), pend).trim());
                            wiersz = wiersz.substring(pend + 1);
                        }
                    }
                }
                if (wiersz.contains("<tr><td>")) {
                    ArrayList<Object> row = new ArrayList<Object>();
                    while (wiersz.contains("<td>")) {
                        start = wiersz.indexOf("<td>") + "<td>".length();
                        end = wiersz.indexOf("</td>");
                        row.add(wiersz.substring(start, end));
                        wiersz = wiersz.substring(end + 1);
                    }
                    result.addRow(row);
                }
            }
            rs = result;
            return result;
        } catch (final MalformedURLException e) {
            warning.setNextException(new SQLException("Błąd tworzenia adresu URL! Sprawdź klasę KnipJDBC\n" + e.getMessage()));
            System.err.println("Błąd tworzenia adresu URL! Sprawdź klasę KnipJDBC\n" + e.getMessage());
            return null;
        } catch (final IOException e) {
            warning.setNextException(new SQLException("Błąd strumienia I/O! Sprawdź klasę KnipJDBC\n" + e.getMessage()));
            System.err.println("Błąd strumienia I/O! Sprawdź klasę KnipJDBC\n" + e.getMessage());
            return null;
        } catch (final URISyntaxException e) {
            warning.setNextException(new SQLException("Błąd tłumaczenia URL! Sprawdź klasę KnipJDBC" + e.getMessage()));
            System.err.println("Błąd tłumaczenia URL! Sprawdź klasę KnipJDBC\n" + e.getMessage());
            return null;
        }
    }
