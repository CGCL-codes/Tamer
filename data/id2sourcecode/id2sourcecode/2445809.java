    public boolean login(String username, String password) {
        boolean authenticated = false;
        try {
            BufferedReader inCtrl;
            inCtrl = new BufferedReader(new InputStreamReader(socketCtrl.getInputStream()));
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            PrintWriter outCtrl;
            outCtrl = new PrintWriter(socketCtrl.getOutputStream(), true);
            outCtrl.println("LOGIN " + username);
            String response = new String(inCtrl.readLine());
            if (!response.trim().equals("INVALID")) {
                String salt = new String(inCtrl.readLine());
                String salts[] = null;
                salts = salt.split("\\.");
                String data = new String(salts[0] + password + salts[1]);
                byte buf[] = data.getBytes();
                md.update(buf);
                byte hash[] = md.digest();
                for (int i = 0; i < hash.length; i++) {
                    outCtrl.print((int) hash[i]);
                }
                outCtrl.print("\n");
                outCtrl.flush();
                if (inCtrl.readLine().trim().equals("Access Granted")) {
                    authenticated = true;
                    this.download("");
                } else {
                    authenticated = false;
                }
            } else {
                authenticated = false;
            }
        } catch (Exception e) {
            System.out.println("Failed to login " + e);
            return false;
        }
        return authenticated;
    }
