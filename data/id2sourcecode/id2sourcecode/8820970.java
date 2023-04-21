    public void save() {
        String racine = src.substring(7, src.length());
        String urlRelative = toRelative(url.toString());
        if (urlRelative.contains("/")) {
            String reps = urlRelative.substring(0, urlRelative.lastIndexOf("/") + 1);
            File dossiers = new File(target + "/" + racine + "/" + reps + "/");
            dossiers.mkdirs();
        }
        try {
            File fichier = new File(target + "/" + racine + "/" + urlRelative);
            byte[] donnees = new byte[2048];
            InputStream in = url.openStream();
            FileOutputStream out = new FileOutputStream(fichier);
            int i;
            System.out.println("Downloading " + fichier.toString());
            while ((i = in.read(donnees, 0, donnees.length)) != -1) out.write(donnees, 0, i);
            out.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found : " + this.url.toString());
        } catch (Exception e) {
            System.out.println("Connection failed to " + this.url.toString());
        }
    }
