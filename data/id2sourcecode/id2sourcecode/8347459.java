    public void setEnabled(boolean b) {
        try {
            final URL url = new URL("http://" + ip + "/preset.htm?led" + numero + "=" + (b ? '0' : '1'));
            final URLConnection uc = url.openConnection();
            final InputStreamReader input = new InputStreamReader(uc.getInputStream());
            final BufferedReader in = new BufferedReader(input);
            in.read();
            in.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
