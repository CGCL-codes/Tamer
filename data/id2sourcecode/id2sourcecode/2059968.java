    private void readURL() {
        String url = cmd.getURLString(null);
        out("\nreading from URL:\n" + url + "\nto file\n" + tmpfs.getPath());
        URL mpc_url = null;
        try {
            mpc_url = new URL(url);
        } catch (MalformedURLException ex) {
            exit(CmdData.ERR_INPUT_URL + url);
        }
        long apx_len = 0;
        try {
            InputStream is = mpc_url.openStream();
            apx_len = is.available();
            input_source = new BufferedReader(new InputStreamReader(is));
        } catch (IOException ex) {
            exit(CmdData.ERR_INPUT_WEB + url);
        }
        copyData(input_source, tmpfs, apx_len, url);
        try {
            input_source.close();
        } catch (IOException ex) {
        }
        out("\nreading from URL done");
    }
