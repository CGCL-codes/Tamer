    public static String trouveEncodingSource(String source, GestionnaireErreur gest) {
        String sourceEncoding = "";
        File file = new File(source);
        if (verifFichier(file, gest)) {
            try {
                URL url = file.toURI().toURL();
                URLConnection connection = url.openConnection();
                sourceEncoding = connection.getContentEncoding();
            } catch (MalformedURLException mue) {
                gest.setException(mue);
                gest.gestionErreur();
            } catch (IOException ioe) {
                gest.setException(ioe);
                gest.gestionErreur();
            }
        }
        if (sourceEncoding == null) {
            try {
                File fsource = new File(source);
                Charset guessedCharset = CharsetToolkit.guessEncoding(fsource, 4096, Charset.defaultCharset());
                if (guessedCharset != null) {
                    sourceEncoding = guessedCharset.name();
                }
            } catch (FileNotFoundException fnfe) {
                gest.setException(fnfe);
                gest.gestionErreur();
            } catch (IOException ioe) {
                gest.setException(ioe);
                gest.gestionErreur();
            }
        }
        return sourceEncoding;
    }
