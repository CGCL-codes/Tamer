    public List<ArtistMABean> searchArtistsFromMAWebSite(String name) throws Exception {
        URL fileURL = new URL("http://www.metal-archives.com/search.php?string=" + name + "&type=band");
        URLConnection urlConnection = fileURL.openConnection();
        InputStream httpStream = urlConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpStream, "ISO-8859-1"));
        List<ArtistMABean> artists = new ArrayList<ArtistMABean>();
        ArtistMABean ab = new ArtistMABean();
        boolean typePageTrouve = false;
        boolean plusieursReponses = false;
        boolean nomArtisteTrouve = false;
        boolean logoArtisteTrouve = false;
        boolean paysArtisteTrouve = false;
        boolean idArtisteTrouve = false;
        String ligne;
        String nomArtiste;
        StringBuffer sb = new StringBuffer("");
        while ((ligne = br.readLine()) != null) {
            log.debug("==> " + ligne);
            if (!typePageTrouve) {
                if (ligne.indexOf("Encyclopaedia Metallum - ") != -1) {
                    plusieursReponses = false;
                    typePageTrouve = true;
                    artists.add(ab);
                } else if (ligne.indexOf("Search results") != -1) {
                    plusieursReponses = true;
                    typePageTrouve = true;
                    ligne = br.readLine();
                }
            }
            if (typePageTrouve && !plusieursReponses) {
                if (!nomArtisteTrouve && ligne.indexOf("Encyclopaedia Metallum - ") != -1) {
                    nomArtiste = ligne.substring(ligne.indexOf("-") + 2, ligne.indexOf("<", 3));
                    log.debug("Nom artiste : " + nomArtiste);
                    ab.setNom(nomArtiste);
                    nomArtisteTrouve = true;
                } else if (!logoArtisteTrouve && ligne.indexOf("<img") != -1) {
                    String url = ligne.substring(ligne.indexOf("http"), ligne.indexOf("\"", 10));
                    log.debug("URL logo : " + url);
                    ab.setURLLogo(url);
                    logoArtisteTrouve = true;
                } else if (!paysArtisteTrouve && ligne.indexOf("browseC.php") != -1) {
                    String pays = ligne.substring(ligne.indexOf("=", 13) + 1, ligne.indexOf("\"", 15));
                    log.debug("Pays id : " + pays);
                    ab.setPaysMaid(Integer.parseInt(pays));
                    paysArtisteTrouve = true;
                } else if (!idArtisteTrouve && ligne.indexOf("editdata.php") != -1) {
                    String maid = ligne.substring(ligne.indexOf("=", 20) + 1, ligne.indexOf("\"", 20));
                    log.debug("MAID : " + maid);
                    ab.setMaid(Long.parseLong(maid));
                    idArtisteTrouve = true;
                } else if (ligne.indexOf(">Discography<") != -1) {
                    while (ligne.indexOf(">Links<") == -1 && ligne.indexOf("Back to the Encyclopaedia Metallum") == -1) {
                        log.debug("==> " + ligne);
                        sb.append(ligne.trim());
                        ligne = br.readLine();
                    }
                    break;
                }
            } else if (typePageTrouve && plusieursReponses) {
                int posBandPhp = 0;
                while (ligne.indexOf("band.php", posBandPhp + 1) != -1) {
                    posBandPhp = ligne.indexOf("band.php", posBandPhp + 1);
                    long maid = Long.parseLong(ligne.substring(posBandPhp + 12, ligne.indexOf("'", posBandPhp)));
                    String nom = ligne.substring(ligne.indexOf("'", posBandPhp) + 2, ligne.indexOf("<", posBandPhp));
                    log.debug("Maid : " + maid);
                    log.debug("Nom : " + nom);
                    ab = new ArtistMABean();
                    ab.setMaid(maid);
                    ab.setNom(nom);
                    artists.add(ab);
                }
                break;
            }
        }
        if (typePageTrouve && !plusieursReponses) {
            ligne = sb.toString();
            ligne = ligne.replaceAll("\t", "");
            String[] lignes = ligne.split(">");
            for (int i = 0; i < lignes.length; i++) {
                if (lignes[i].startsWith("<a")) {
                    log.debug("Ligne 1 : " + lignes[i]);
                    String rmaidS = lignes[i].substring(lignes[i].indexOf("=", lignes[i].indexOf("href") + 7) + 1, lignes[i].length() - 1);
                    log.debug("rmaid : " + rmaidS);
                    int rmaid = Integer.parseInt(rmaidS);
                    log.debug("Ligne 2 : " + lignes[++i]);
                    String nom = lignes[i].substring(0, lignes[i].indexOf("<"));
                    log.debug("Nom : " + nom);
                    i = i + 3;
                    log.debug("Ligne 3 : " + lignes[i]);
                    String type = lignes[i].substring(0, lignes[i].indexOf(","));
                    log.debug("Type : " + type);
                    String anneeS = lignes[i].substring(lignes[i].indexOf(",") + 2, lignes[i].indexOf("<"));
                    log.debug("Annee : " + anneeS);
                    int annee = Integer.parseInt(anneeS);
                    ab.addRelease(new ReleaseMABean(rmaid, ab.getMaid(), nom, MetalArchivesDAOImpl.getInstance().getMAReleaseTypeId(type), annee));
                }
            }
            getAllReleasesDataFromMAWebSite(ab, false);
        }
        br.close();
        httpStream.close();
        return artists;
    }
