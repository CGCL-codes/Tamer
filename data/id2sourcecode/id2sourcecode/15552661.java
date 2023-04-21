    public void checkUpdate() {
        try {
            URL url = new URL("http://www.operatorplease.de/versions");
            URLConnection uc = url.openConnection();
            uc.connect();
            Properties props = new Properties();
            try {
                InputStream is = uc.getInputStream();
                props.load(is);
                is.close();
            } catch (FileNotFoundException e) {
                System.out.println("Properties file 'versions' not found!");
            }
            String version = props.getProperty(ID);
            System.out.println("update: id=" + ID + ", version=" + version);
            if (isNeverVersions(version)) {
                JOptionPane.showMessageDialog(this, mes.getString("Update.found") + "\nVersion: " + version, mes.getString("Update.check"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, mes.getString("Update.none"), mes.getString("Update.check"), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String err = mes.getString("Update.error") + "\n" + ex.getLocalizedMessage();
            err += "\n\n";
            err += mes.getString("Update.checkWebsite");
            JOptionPane.showMessageDialog(this, err, mes.getString("Update.check"), JOptionPane.ERROR_MESSAGE);
            System.err.println(err);
        }
    }
