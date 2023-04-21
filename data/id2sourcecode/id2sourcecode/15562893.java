    private String doPost(String query, String location) {
        assert (this.db != null && !this.db.equals("")) : "db ne peut pas etre null ou vide";
        assert (this.dbwLocationIndex != null && !this.dbwLocationIndex.equals("")) : "dbwLocationIndex ne peut pas etre vide ou null";
        assert (this.dbwPwd != null) : "dbwPwd ne peut pas etre null";
        assert (this.dbwUserName != null) : "dbwUserName ne peut pas etre null";
        assert (this.encoding != null && !this.encoding.equals("")) : "encoding ne peut pas etre null ou vide";
        assert (this.host != null && !this.host.equals("")) : "host ne peut pas etre null ou vide";
        String toReturn = "";
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
            String donnees = URLEncoder.encode("request", encoding) + "=" + URLEncoder.encode(query, encoding);
            donnees += "&" + URLEncoder.encode("username", encoding) + "=" + URLEncoder.encode(this.dbwUserName, encoding);
            donnees += "&" + URLEncoder.encode("psw", encoding) + "=" + URLEncoder.encode(this.dbwPwd, encoding);
            donnees += "&" + URLEncoder.encode("db", encoding) + "=" + URLEncoder.encode(this.db, encoding);
            donnees += "&" + URLEncoder.encode("host", encoding) + "=" + URLEncoder.encode(this.host, encoding);
            URL url = new URL(location);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(donnees);
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                toReturn += ligne;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return toReturn;
    }
