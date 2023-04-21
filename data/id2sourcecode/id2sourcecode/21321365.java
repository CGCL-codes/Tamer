    public static Transcription fabriqueTranscription(String fNoir, String fBraille, GestionnaireErreur g, boolean reverse) {
        Transcription retour = null;
        Convertisseur c;
        Presentateur p;
        Transcodeur t;
        String noirEncoding = (reverse) ? "UTF-8" : ConfigNat.getCurrentConfig().getNoirEncoding();
        String brailleEncoding = ConfigNat.getCurrentConfig().getBrailleEncoding();
        String tableBraille = ConfigNat.getCurrentConfig().getTableBraille();
        String sourceMimeType = "";
        g.afficheMessage("\nAnalyse du fichier source " + fNoir, Nat.LOG_SILENCIEUX);
        if (fNoir.startsWith("http://") || fNoir.startsWith("www")) {
            URL url;
            try {
                url = new URL(fNoir);
                URLConnection urlCon = url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
                File ftmp = new File(fTempHtml);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ftmp)));
                String ligne = "";
                while ((ligne = br.readLine()) != null) {
                    bw.write(ligne);
                }
                br.close();
                bw.close();
                fNoir = fTempHtml;
            } catch (MalformedURLException e) {
                g.afficheMessage("\n** adresse internet non valide", Nat.LOG_SILENCIEUX);
            } catch (IOException e) {
                g.afficheMessage("\n** erreur d'entrée sortie lors de la création du fichier source temporaire sur le disque", Nat.LOG_SILENCIEUX);
            }
        }
        if (verifFichier(new File(fNoir), g) && !reverse || reverse && verifFichier(new File(fBraille), g)) {
            if (reverse) {
                if (!noirEncoding.equals("automatique") && !noirEncoding.equals("")) {
                    g.afficheMessage("\n** Utilisation de l'encodage " + noirEncoding + " spécifié dans les options pour le fichier braille\n", Nat.LOG_NORMAL);
                } else {
                    noirEncoding = trouveEncodingSource(fNoir, g);
                    if (noirEncoding == null || noirEncoding.equals("")) {
                        noirEncoding = Charset.defaultCharset().name();
                        g.afficheMessage("\n** Impossible de détecter l'encodage du fichier braille." + "\n** Utilisation de l'encodage par défaut: " + noirEncoding + "\n", Nat.LOG_NORMAL);
                    } else {
                        g.afficheMessage("\n** Détection automatique de l'encodage du fichier braille: " + noirEncoding + "\n", Nat.LOG_NORMAL);
                    }
                }
                FileToolKit.convertBrailleFile(fBraille, fTempTan, ConfigNat.getUserBrailleTableFolder() + "Brltab.ent", ConfigNat.getInstallFolder() + "xsl/tablesEmbosseuse/brailleUTF8.ent", ConfigNat.getCurrentConfig().getBrailleEncoding(), "UTF-8", g);
                c = new ConvertisseurTan(fTempTan, fTempXML, '⠀');
                if (noirEncoding.equals("automatique")) {
                    if (brailleEncoding.equals("automatique")) {
                        noirEncoding = Charset.defaultCharset().name();
                    } else {
                        noirEncoding = brailleEncoding;
                    }
                }
                t = new TranscodeurNormal(fTempXML, fTempXML2, "UTF-8", g);
                t.setSens(true);
                p = new PresentateurSans(g, noirEncoding, fTempXML2, fNoir, tableBraille);
                retour = new Transcription(g, c, t, p);
            } else {
                sourceMimeType = trouveMimeTypeSource(fNoir, g);
                g.afficheMessage("\n** Le fichier source est de type " + sourceMimeType, Nat.LOG_NORMAL);
                if (sourceMimeType.equals("text/plain")) {
                    if (!noirEncoding.equals("automatique") && !noirEncoding.equals("")) {
                        g.afficheMessage("\n** Utilisation de l'encodage " + noirEncoding + " spécifié dans les options pour le fichier source\n", Nat.LOG_NORMAL);
                    } else {
                        noirEncoding = trouveEncodingSource(fNoir, g);
                        if (noirEncoding == null || noirEncoding.equals("")) {
                            noirEncoding = Charset.defaultCharset().name();
                            g.afficheMessage("\n** Impossible de détecter l'encodage du fichier source.\n** Utilisation de l'encodage par défaut: " + noirEncoding + "\n", Nat.LOG_NORMAL);
                        } else {
                            g.afficheMessage("\n** Détection automatique de l'encodage du fichier source: " + noirEncoding + "\n", Nat.LOG_NORMAL);
                        }
                    }
                    if (ConfigNat.getCurrentConfig().getTraiterMaths() || ConfigNat.getCurrentConfig().getTraiterMusique()) {
                        c = new ConvertisseurTexteMixte(fNoir, fTempXML, noirEncoding);
                    } else {
                        c = new ConvertisseurTexte(fNoir, fTempXML, noirEncoding);
                    }
                } else if (sourceMimeType.equals("") || fNoir.endsWith("odt") || fNoir.endsWith("sxw")) {
                    g.afficheMessage("\n** Le fichier source est identifié comme document openoffice ", Nat.LOG_NORMAL);
                    c = new ConvertisseurOpenOffice(fNoir, fTempXML);
                } else if (sourceMimeType.equals("text/html")) {
                    g.afficheMessage("\n** Le fichier source est identifié comme document xml/html", Nat.LOG_NORMAL);
                    c = new ConvertisseurXML(fNoir, fTempXML);
                } else if (fNoir.endsWith("xhtml")) {
                    g.afficheMessage("\n** Le fichier source est identifié comme document xhtml", Nat.LOG_NORMAL);
                    c = new ConvertisseurXML(fNoir, fTempXML);
                } else if (sourceMimeType.equals("application/xml")) {
                    if (fNoir.endsWith("nat") || fNoir.endsWith("zob")) {
                        g.afficheMessage("\n** Le fichier source est identifié comme un format interne", Nat.LOG_NORMAL);
                        c = new ConvertisseurSans(fNoir, fTempXML);
                    } else {
                        g.afficheMessage("\n** Le fichier source est identifié comme document xml/html", Nat.LOG_NORMAL);
                        c = new ConvertisseurXML(fNoir, fTempXML);
                    }
                } else {
                    g.afficheMessage("\n** Utilisation de JODConverter", Nat.LOG_NORMAL);
                    c = new Convertisseur2ODT(fNoir, fTempXML);
                }
                if (brailleEncoding.compareTo("automatique") == 0) {
                    if (noirEncoding.equals("automatique")) {
                        brailleEncoding = Charset.defaultCharset().name();
                    } else {
                        brailleEncoding = noirEncoding;
                    }
                }
                p = new PresentateurMEP(g, brailleEncoding, fTempXML2, fBraille, tableBraille);
                if (!new File(ConfigNat.getCurrentConfig().getDicoCoup()).exists() || !new File(ConfigNat.getUserTempFolder() + "hyphens.xsl").exists()) {
                    g.afficheMessage("\n** Création du fichier de coupure à partir de " + ConfigNat.getCurrentConfig().getDicoCoup(), Nat.LOG_NORMAL);
                    HyphenationToolkit.fabriqueDicoNat(ConfigNat.getCurrentConfig().getDicoCoup(), xslHyphen, "UTF-8");
                } else {
                    g.afficheMessage("\n** Utilisation du dictionnaire de coupure existant", Nat.LOG_NORMAL);
                }
                if (fNoir.endsWith("nat")) {
                    g.afficheMessage("\n** Le fichier source est identifié comme format interne de présentation", Nat.LOG_NORMAL);
                    t = new TranscodeurSans(fTempXML, fTempXML2, "UTF-8", g);
                } else {
                    t = new TranscodeurNormal(fTempXML, fTempXML2, "UTF-8", g);
                }
                retour = new Transcription(g, c, t, p);
            }
        }
        return retour;
    }
