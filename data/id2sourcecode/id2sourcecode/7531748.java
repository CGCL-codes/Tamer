    public void getTracksFromMAWebSite(ReleaseMABean rb) throws Exception {
        URL fileURL = new URL("http://www.metal-archives.com/release.php?id=" + rb.getMaid());
        URLConnection urlConnection = fileURL.openConnection();
        InputStream httpStream = urlConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpStream, "ISO-8859-1"));
        String ligne;
        int discNum = 1;
        boolean tracksTrouve = false;
        while ((ligne = br.readLine()) != null) {
            log.debug("==> " + ligne);
            if (!tracksTrouve && ligne.indexOf("<tr><td>1.<") != -1) {
                String[] lignes = ligne.split(">");
                for (int i = 0; i < lignes.length; i++) {
                    log.debug("--> " + lignes[i]);
                    if (lignes[i].indexOf("Disc ") != -1) {
                        String discNumS = lignes[i].substring(lignes[i].indexOf("Disc ") + 5, lignes[i].indexOf("<"));
                        log.debug("Disque Numero : " + discNumS);
                        discNum = Integer.parseInt(discNumS);
                    } else if (lignes[i].indexOf(".</td") != -1) {
                        String numS = lignes[i].substring(0, lignes[i].indexOf("."));
                        log.debug("Num : " + numS);
                        int num = Integer.parseInt(numS);
                        log.debug("--> " + lignes[++i]);
                        log.debug("--> " + lignes[++i]);
                        String titre = lignes[i].substring(0, lignes[i].indexOf("<"));
                        log.debug("Titre : " + titre);
                        log.debug("--> " + lignes[++i]);
                        log.debug("--> " + lignes[++i]);
                        int duree = -1;
                        if (lignes[i].indexOf(":") != -1) {
                            String dureeS = lignes[i].substring(0, lignes[i].indexOf("<"));
                            log.debug("Duree : " + dureeS);
                            duree = Integer.parseInt(dureeS.substring(0, dureeS.indexOf(":"))) * 60;
                            duree += Integer.parseInt(dureeS.substring(dureeS.indexOf(":") + 1));
                        }
                        log.debug("--> " + lignes[++i]);
                        log.debug("--> " + lignes[++i]);
                        int maid = -1;
                        if (lignes[i].indexOf("openLyrics") != -1) {
                            String maidS = lignes[i].substring(lignes[i].indexOf("(") + 1, lignes[i].indexOf(")"));
                            log.debug("Maid : " + maidS);
                            maid = Integer.parseInt(maidS);
                        } else {
                            log.debug("--> " + lignes[++i]);
                            log.debug("--> " + lignes[++i]);
                        }
                        rb.addTrack(new TrackMABean(maid, rb.getMaid(), discNum, titre, duree, num));
                    }
                }
                tracksTrouve = true;
            } else if (ligne.indexOf("<a href=\"/images") != -1) {
                String URLCover = "http://www.metal-archives.com" + ligne.substring(ligne.indexOf("/images"), ligne.indexOf("\"", 15));
                log.debug("Cover = " + URLCover);
                rb.setURLCover(URLCover);
                break;
            }
        }
        if (rb.getURLCover() != null) {
            saveCoverToLocalhost(rb);
        }
        br.close();
        httpStream.close();
    }
