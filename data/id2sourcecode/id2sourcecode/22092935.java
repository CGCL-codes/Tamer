    private void login() {
        ProgressDialog p = ProgressDialogManager.getProgressDialog();
        p.setNote("Logging into Sanford");
        try {
            URL url = new URL(PROTOCOL, HOST, "/sanford/Public/Home/Login.asp");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStream ostream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(ostream);
            writer.print("username=" + username + "&password=" + password);
            writer.close();
            cookie = connection.getHeaderField("Set-Cookie");
            connected = true;
        } catch (java.io.IOException io) {
            DesktopManager.showErrorMessage("Can't connect to Sanford");
        }
        ProgressDialogManager.closeProgressDialog();
    }
