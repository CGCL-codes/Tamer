    public static void testPut(String word) {
        try {
            String req = servUrl + "?word=" + word;
            System.out.println("\nPUT " + req);
            HttpURLConnection con = (HttpURLConnection) (new URL(req)).openConnection();
            con.setRequestMethod("PUT");
            con.setDoOutput(true);
            con.setRequestProperty("ContentType", "text/xml");
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            File file = new File(word + ".xml");
            FileReader reader = new FileReader(file);
            int character;
            while ((character = reader.read()) != -1) out.write(character);
            out.close();
            reader.close();
            int responseCode = con.getResponseCode();
            InputStream err = con.getErrorStream();
            int c;
            if (err != null) while ((c = err.read()) != -1) {
                System.out.print(new Character((char) c));
            }
            System.out.println("ResponseCode: " + responseCode + "\n");
            con.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
