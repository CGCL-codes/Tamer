    public void update(String target, String cfgVersion) throws MalformedURLException, FileNotFoundException, IOException {
        URL url = new URL(target);
        String[] urlSplit = target.split("/");
        this.fileName = urlSplit[urlSplit.length - 1];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(Main.getHomeDir() + "tmp-" + this.fileName));
        URLConnection urlConnection = url.openConnection();
        InputStream in = urlConnection.getInputStream();
        byte[] buffer = new byte[1024];
        int numRead;
        while ((numRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, numRead);
        }
        in.close();
        out.close();
        XMLController xmlC = new XMLController();
        String newFileVersion = xmlC.readCfgVersion(Main.getHomeDir() + "tmp-" + this.fileName);
        if (new File(Main.getHomeDir() + this.fileName).exists()) {
            if (Double.parseDouble(newFileVersion) > Double.parseDouble(cfgVersion)) {
                new File(Main.getHomeDir() + this.fileName).delete();
                new File(Main.getHomeDir() + "tmp-" + this.fileName).renameTo(new File(Main.getHomeDir() + this.fileName));
                this.result = "ConfigFile Updated Succesfully";
            } else {
                new File(Main.getHomeDir() + "tmp-" + this.fileName).delete();
            }
        } else {
            new File(Main.getHomeDir() + "tmp-" + this.fileName).renameTo(new File(Main.getHomeDir() + this.fileName));
            this.result = "ConfigFile Loaded";
        }
    }
