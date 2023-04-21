    private static String trouveMimeTypeSource(String source, GestionnaireErreur gest) {
        String retour = "";
        File file = new File(source);
        if (verifFichier(file, gest)) {
            try {
                URL url = file.toURI().toURL();
                URLConnection connection = url.openConnection();
                retour = connection.getContentType();
            } catch (MalformedURLException mue) {
                gest.setException(mue);
                gest.gestionErreur();
            } catch (IOException ioe) {
                gest.setException(ioe);
                gest.gestionErreur();
            }
        }
        return retour;
    }
