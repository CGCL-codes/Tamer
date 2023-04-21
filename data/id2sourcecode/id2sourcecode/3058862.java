            @Override
            protected void onSubmit() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = sdf.parse(field.getValue());
                    if (date.before(ficheDossier.getDateFermeture())) {
                        String fermeture = "(" + sdf.format(ficheDossier.getDateFermeture()) + ")";
                        VerserDossierPage.this.error("La date de versement ne doit pas être inférieure à la date de fermeture " + fermeture);
                    } else {
                        ficheDossier.setDateVersementReelle(date);
                        ficheDossier.setUtilisateurVersementReel(getUtilisateurCourant());
                        new FGDDelegate().sauvegarder((FicheDossier) ficheDossier, getUtilisateurCourant());
                        ConversationManager conversationManager = FGDSpringUtils.getConversationManager();
                        conversationManager.commitTransaction();
                        RequestCycle.get().setResponsePage(ConsulterDossierPage.class, new PageParameters("id=" + ficheDossier.getId()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
