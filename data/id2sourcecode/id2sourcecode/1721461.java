    public void saveFile(int taille_max) {
        String racine = src.substring(7, src.length());
        String urlRelative = GenerateurDeNom.genererNomRelatif(url.toString(), src);
        if (urlRelative.contains("/")) {
            String reps = urlRelative.substring(0, urlRelative.lastIndexOf("/"));
            File dossiers = new File(destination + "/" + racine + "/" + reps + "/");
            dossiers.mkdirs();
        }
        try {
            File fichier = new File(destination + "/" + racine + "/" + urlRelative);
            byte[] donnees = new byte[taille_max];
            InputStream in = url.openStream();
            FileOutputStream out = new FileOutputStream(fichier);
            int i;
            while ((i = in.read(donnees, 0, donnees.length)) != -1) {
                out.write(donnees, 0, i);
            }
            out.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
