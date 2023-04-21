    public void doPost(String adresse) {
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
            String donnees = URLEncoder.encode("clef", "UTF-8") + "=" + URLEncoder.encode("valeur", "UTF-8");
            donnees += "&" + URLEncoder.encode("autreClef", "UTF-8") + "=" + URLEncoder.encode("autreValeur", "UTF-8");
            URL url = new URL(adresse);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(donnees);
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                System.out.println(ligne);
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
    }
