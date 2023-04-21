    public static final void checkVersion(Shell shell) {
        String localVersion = VERSION;
        String remoteVersion = "";
        try {
            URL url = new URL(BASE_URL + REMOTE_VERSION_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = in.readLine();
            remoteVersion = new String(line.trim());
            in.close();
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }
        if (!localVersion.equals(remoteVersion)) {
            MessageBox box = new MessageBox(shell);
            box.setMessage(NLSMessages.getString("Mediator.NewVersion", localVersion, remoteVersion));
            box.open();
            return;
        }
    }
