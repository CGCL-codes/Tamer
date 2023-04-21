    public CConfigFile(JPanel pan, URL documentBase) {
        try {
            panel = pan;
            URL in_url = new URL(documentBase, "tobedone.conf");
            InputStream ins = in_url.openStream();
            InputStreamReader insr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(insr);
            readData(in);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(panel, "Couldn't load " + "config file :\n" + e);
        }
    }
