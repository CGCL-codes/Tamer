    private boolean login(String username, String password) {
        String pwdtextfieldname = "";
        try {
            URL url = new URL("https://netlogin.kuleuven.be/cgi-bin/wayf.pl?inst=kuleuven&lang=nl&submit=Ga+verder+/+Continue");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            Log.d("Username", username);
            Log.d("Password", password);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            boolean success = false;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("autocomplete") != -1) {
                    int start = line.indexOf("name=\"") + 6;
                    line = line.substring(start);
                    int end = line.indexOf("\"");
                    line = line.substring(0, end);
                    pwdtextfieldname = line;
                    success = true;
                }
            }
            if (!success) return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            URL url = new URL("https://netlogin.kuleuven.be/cgi-bin/netlogin.pl");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setReadTimeout(10 * 1000);
            PrintWriter w = new PrintWriter(connection.getOutputStream());
            w.print("inst=kuleuven&lang=nl&uid=" + username + "&" + pwdtextfieldname + "=" + password + "&submit=Login");
            w.flush();
            w.close();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            boolean success = false;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("Login geslaagd") != -1) {
                    success = true;
                }
            }
            if (!success) return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
