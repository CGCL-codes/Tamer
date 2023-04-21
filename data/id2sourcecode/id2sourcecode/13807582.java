    public boolean checkUpdate() {
        boolean retour = true;
        gest.afficheMessage("Recherche d'une mise à jour de NAT...", LOG_VERBEUX);
        URL url;
        try {
            url = new URL(CURRENT_VERSION_ADDRESS);
            URLConnection urlCon = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String ligne = br.readLine();
            br.close();
            if (Integer.parseInt(ligne) > ConfigNat.getSvnVersion()) {
                updateAvailable = true;
            }
        } catch (NumberFormatException nfe) {
            gest.afficheMessage("\n** pas de connexion web pour vérifier la présence de mise à jour", Nat.LOG_SILENCIEUX);
            retour = false;
        } catch (MalformedURLException e) {
            gest.afficheMessage("\n** adresse internet " + CURRENT_VERSION_ADDRESS + " non valide", Nat.LOG_SILENCIEUX);
            retour = false;
        } catch (IOException e) {
            gest.afficheMessage("\n** erreur d'entrée sortie lors de la vérification de l'existence d'une mise à jour", Nat.LOG_SILENCIEUX);
            retour = false;
        }
        return retour;
    }
