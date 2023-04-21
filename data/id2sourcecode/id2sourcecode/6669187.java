        @Override
        public void run() {
            try {
                boolean transactionTerminee = false;
                int TAILLE_BUFFER = 128;
                Socket PESocket = new Socket(PCreatorConstantes.PEXECUTOR_ADRESSE, PCreatorConstantes.PEXECUTOR_PORT_DIALOGUE);
                Application.logger.info("Connexion à PExecutor effectuée");
                PrintStream PEOutputStream = new PrintStream(PESocket.getOutputStream());
                BufferedReader PEAnswer = new BufferedReader(new InputStreamReader(PESocket.getInputStream()));
                Application.logger.info("Envoi des données");
                PEOutputStream.println("Envoi chaine : " + chaine.getNom());
                String reponse = PEAnswer.readLine();
                if (reponse.equals(PCreatorConstantes.PE_OK)) {
                    Socket PEFSocket = new Socket(PCreatorConstantes.PEXECUTOR_ADRESSE, PCreatorConstantes.PEXECUTOR_PORT_FICHIERS);
                    PrintStream PEFOutputStream = new PrintStream(PEFSocket.getOutputStream());
                    XMLSerializer serializer = new XMLSerializer(PEFOutputStream, format);
                    serializer.serialize(dom);
                    Application.logger.info("Chaine envoyée");
                    PEFSocket.close();
                    reponse = PEAnswer.readLine();
                    while (reponse != null && reponse.matches(PCreatorConstantes.PE_ENVOI_SERVICE + ".*")) {
                        Application.logger.debug(reponse);
                        Pattern pattern = Pattern.compile(PCreatorConstantes.PE_ENVOI_SERVICE + "(.*)");
                        Matcher matcher = pattern.matcher(reponse);
                        if (matcher.find()) {
                            String idService = matcher.group(1);
                            String emplacement = null;
                            try {
                                emplacement = dom.getElementById(idService).getAttribute("emplacement");
                                byte[] bbuf = new byte[TAILLE_BUFFER];
                                BufferedInputStream bim = new BufferedInputStream(new FileInputStream(emplacement));
                                PEOutputStream.println(PCreatorConstantes.PE_OK);
                                reponse = PEAnswer.readLine();
                                if (reponse != null && reponse.equals(PCreatorConstantes.PE_OK)) {
                                    PEFSocket = new Socket(PCreatorConstantes.PEXECUTOR_ADRESSE, PCreatorConstantes.PEXECUTOR_PORT_FICHIERS);
                                    PEFOutputStream = new PrintStream(PEFSocket.getOutputStream());
                                    int read = bim.read(bbuf, 0, TAILLE_BUFFER);
                                    while (read >= 0) {
                                        PEFOutputStream.write(bbuf, 0, read);
                                        read = bim.read(bbuf, 0, TAILLE_BUFFER);
                                    }
                                    PEFSocket.close();
                                    Application.logger.debug("Service " + idService + " envoyé avec succés");
                                    reponse = PEAnswer.readLine();
                                }
                            } catch (NullPointerException e) {
                                Application.logger.error("Service " + idService + " non trouvé.\n	Transaction annulée.");
                                PEOutputStream.println("Erreur : Service " + idService + "non trouvé");
                                break;
                            } catch (FileNotFoundException e) {
                                Application.logger.error("Impossible d'envoyer le service " + idService + ", emplacement invalide : " + emplacement + "\n	Transaction annulée.");
                                PEOutputStream.println("Erreur : Impossible d'envoyer le service " + idService + ", emplacement invalide : " + emplacement);
                                break;
                            }
                        }
                    }
                    if (reponse.equals(PCreatorConstantes.PE_FIN_TRANSMISSION)) {
                        Application.logger.info("Données envoyées avec succés, fermeture de la connexion.");
                        transactionTerminee = true;
                    }
                }
                if (!transactionTerminee) {
                    Application.logger.error("Une erreur s'est produite lors de l'envoi de la chaîne.\n	Dernier message du serveur : " + reponse);
                }
                PEOutputStream.close();
                PESocket.close();
            } catch (ConnectException ce) {
                Application.logger.error("Connexion à PExecutor échouée : " + ce.getLocalizedMessage());
            } catch (UnknownHostException e) {
                Application.logger.error("Connexion à PExecutor échouée (héte inconnu) : " + e.getLocalizedMessage());
            } catch (IOException e) {
                Application.logger.error("Erreur lors de l'envoi de la chaîne à PExecutor: " + e.getLocalizedMessage());
            }
        }
