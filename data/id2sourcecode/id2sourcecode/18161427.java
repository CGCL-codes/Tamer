    public String[] getResponses(String account, String password, String seed) {
        try {
            URL url = new URL("http", "login.yahoo.com", 80, getAuthUrlString(account, password));
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("Host", "login.yahoo.com");
            BufferedReader is = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String data = is.readLine();
            String ticket = parseAuthData(data);
            is.close();
            return new String[] { ticket };
        } catch (NoSuchAlgorithmException e) {
            throw (RuntimeException) new IllegalStateException().initCause(e);
        } catch (IOException e) {
            throw (RuntimeException) new IllegalStateException().initCause(e);
        }
    }
