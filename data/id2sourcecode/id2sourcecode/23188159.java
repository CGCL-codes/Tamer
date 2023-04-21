    public static void setParametres(String url, String nom, String motdepasse, int type, String emplacement, int tailleBlocs, int nbThreads, int tailleCache) {
        try {
            File f = new File("param.de");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Parametres.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            FileOutputStream fs = null;
            try {
                fs = new FileOutputStream(f);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Parametres.class.getName()).log(Level.SEVERE, null, ex);
            }
            ObjectOutputStream os = null;
            try {
                os = new ObjectOutputStream(fs);
            } catch (IOException ex) {
                Logger.getLogger(Parametres.class.getName()).log(Level.SEVERE, null, ex);
            }
            os.writeObject(url);
            os.writeObject(nom);
            os.writeObject(motdepasse);
            os.writeObject(emplacement);
            os.writeInt(type);
            os.writeInt(tailleBlocs);
            os.writeInt(nbThreads);
            os.writeInt(tailleCache);
            os.close();
            fs.close();
            Parametres.url = url;
            Parametres.nom = nom;
            Parametres.motdepasse = motdepasse;
            Parametres.emplacement = emplacement;
            Parametres.type = type;
            Parametres.tailleBlocs = tailleBlocs;
            Parametres.nbThreads = nbThreads;
            Parametres.tailleCache = tailleCache;
        } catch (IOException ex) {
            Logger.getLogger(Parametres.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
