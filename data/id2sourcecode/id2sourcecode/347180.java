    @Test
    public void HTTPPostTest() {
        try {
            data = URLEncoder.encode("gebruikersNaam", "UTF-8") + "=" + URLEncoder.encode("Koen", "UTF-8") + "&" + URLEncoder.encode("wachtwoord", "UTF-8") + "=" + URLEncoder.encode("abc123", "UTF-8");
            URL url = new URL("http://127.0.0.1/formverwerking.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();
            assertEquals("gebruikersNaam=Koen&wachtwoord=abc123", data);
            assertEquals("gebruikersNaam=Koen&wachtwoord=abc123".length(), data.length());
        } catch (Exception e) {
            fail("The POST data string wasn't put together as expected.");
        }
    }
