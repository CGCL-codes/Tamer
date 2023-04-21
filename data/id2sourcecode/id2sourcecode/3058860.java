    private void initComponents(final FicheDossier ficheDossier) {
        addObjetNumeriqueMultiRequete(ficheDossier);
        Form form = new Form("form");
        add(form);
        String dateVersementPrevue;
        if (ficheDossier.getDateVersementPrevue() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateVersementPrevue = sdf.format(ficheDossier.getDateVersementPrevue());
        } else {
            dateVersementPrevue = "";
        }
        form.add(new Label("dateVersementPrevue", dateVersementPrevue));
        final FormComponent field = getField("dateVersementReelle", ficheDossier);
        form.add(field);
        add(new JavaScriptReference("maskjs", DatePanel.class, "masks.js"));
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        datePickerSettings.setIcon(new ContextPathResourceReference("/ui/images/commun/calendrier.gif"));
        DatePicker datePicker = new DatePicker("datePicker", field, datePickerSettings);
        datePicker.setDateConverter(FGDDateUtils.getDateConverter());
        form.add(datePicker);
        form.add(new Button("cancelButton") {

            @Override
            protected void onSubmit() {
                RequestCycle.get().setResponsePage(ConsulterDossierPage.class, new PageParameters("id=" + ficheDossier.getId()));
            }
        });
        form.add(new Button("actionButton") {

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
        });
    }
