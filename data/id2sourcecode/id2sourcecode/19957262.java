    public static void sendReport(String comment) {
        try {
            URL url = new URL("http://backend-ea.e-ucm.es/reports.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            con.setInstanceFollowRedirects(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            String content = "type=comment" + "&version=medicina_informe" + "&file=" + comment;
            out.writeBytes(content);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            in.readLine();
        } catch (Exception e) {
        }
    }
