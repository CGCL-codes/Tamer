    @Override
    public String topluEmail(String message) {
        try {
            String data = "message=" + URLEncoder.encode(message, "UTF-8");
            URL url = new URL("http://interrailmap.com:8080/MailUtil6/BatchEmail");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
            System.out.println(answer.toString());
            return "Toplu Mail gonderildi!";
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return "HATA 10.09 URL yapisi hatasi";
        } catch (IOException ex) {
            ex.printStackTrace();
            return "HATA 10.09 Baglanti hatasi";
        }
    }
