    public Aspirateur(String url, String d, Filtre f) {
        filtre = f;
        chercheur = new ChercheurDeBalise();
        if (url.endsWith("/")) url = url.substring(0, url.lastIndexOf("/"));
        if (url.contains("index.")) url = url.substring(0, url.indexOf("index."));
        source = url;
        destination = d;
        pagesATraiter = new ListeDePages();
        pagesTraitees = new ListeDePages();
        imagesATelecharger = new ListeDePages();
        cssATelecharger = new ListeDePages();
        jsATelecharger = new ListeDePages();
        pagesATraiter.ajouterPage(new Lien(source, 0));
        try {
            URL _url = new URL(source);
            URLConnection urlConnection = _url.openConnection();
            parser = new Parser(urlConnection);
            System.out.println("---Aspiration des pages HTML");
            while (!pagesATraiter.estVide()) {
                aspirer();
            }
            System.out.println("Fin de l'aspiration des pages");
            System.out.println("\n\n---Aspiration des feuilles de style");
            while (!cssATelecharger.estVide()) {
                telechargerCSS();
            }
            System.out.println("Fin de l'aspiration des feuilles de style");
            System.out.println("\n\n---Aspiration des Javascripts");
            while (!jsATelecharger.estVide()) {
                telechargerJS();
            }
            System.out.println("Fin de l'aspiration des feuilles de style");
            System.out.println("\n\n---Aspiration des images");
            while (!imagesATelecharger.estVide()) {
                telechargerImage();
            }
            System.out.println("Fin de l'aspiration des images");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }
