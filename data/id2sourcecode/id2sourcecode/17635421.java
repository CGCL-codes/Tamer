    private static void TelechargerPluginEtCharger(ControleurDeMenu i, String adresse) {
        URL url;
        try {
            url = new URL(adresse);
        } catch (MalformedURLException e1) {
            System.err.println("impossible d'ouvrir l'URL (url mal formee)" + adresse);
            return;
        }
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e1) {
            System.err.println("impossible d'ouvrir l'URL (destination inaccessible)" + adresse);
            return;
        }
        File f = new File(InformationsSurApplicationServeur.cheminVersPlugins + java.io.File.separatorChar + i.getNomPlugin() + ".jar.new");
        if (f.exists()) {
            if (!f.delete()) {
                System.err.println("la supression du plugin de " + i.getNomPlugin() + " (extention .jar.new) obselete a echoue " + f.getAbsolutePath());
                return;
            }
        }
        try {
            if (f.createNewFile()) {
            } else {
                System.err.println("impossible de creer le fichier .jar (plugins) pour le plugin " + i.getNomPlugin());
            }
        } catch (IOException e) {
            System.err.println("impossible de creer le fichier .jar (plugins) pour le plugin " + i.getNomPlugin() + "\n :" + e.getMessage());
            return;
        }
        java.io.FileOutputStream destinationFile = null;
        try {
            destinationFile = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            System.err.println("impossible d'ouvrir le flux en ecriture pour ecrire le plugin (.jar) " + i.getNomPlugin());
            return;
        }
        byte buffer[] = new byte[512 * 1024];
        int nbLecture;
        try {
            while ((nbLecture = is.read(buffer)) != -1) {
                destinationFile.write(buffer, 0, nbLecture);
            }
        } catch (IOException e) {
            System.err.println("impossible d'ecrire dans le fichier jar plugins de " + i.getNomPlugin());
            return;
        }
        try {
            is.close();
            destinationFile.close();
        } catch (IOException e) {
            System.err.println("impossible de fermer le fichier plugin " + i.getNomPlugin());
            return;
        }
        chargerUnSimpleJar(f.getAbsolutePath());
    }
