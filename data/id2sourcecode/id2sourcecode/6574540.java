        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            final Concept concept = getConcept();
            final ProgressDialog progressDialog = AppFrameDispatcher.getProgressDialog();
            progressDialog.setTitle("VARS - Adding Media");
            final JProgressBar progressBar = progressDialog.getProgressBar();
            progressBar.setMinimum(0);
            progressBar.setMaximum(3);
            progressBar.setIndeterminate(true);
            progressBar.setString("Searching for media");
            progressDialog.setVisible(true);
            final MediaViewPanel p = getMediaViewPanel();
            boolean isUrlValid = false;
            URL url = null;
            try {
                url = new URL(p.getUrlField().getText());
                final InputStream in = url.openStream();
                final int b = in.read();
                isUrlValid = (b > -1);
                if (!isUrlValid) {
                    AppFrameDispatcher.showWarningDialog("Unable to read from " + url.toExternalForm());
                }
            } catch (Exception e1) {
                final String s = "Failed to open URL, " + p.getUrlField().getText();
                AppFrameDispatcher.showWarningDialog(s);
                log.warn(s, e1);
            }
            progressBar.setIndeterminate(false);
            if (isUrlValid) {
                Set mediaSet = concept.getMediaSet();
                for (Iterator i = mediaSet.iterator(); i.hasNext(); ) {
                    Media m = (Media) i.next();
                    if (m.getUrl().equalsIgnoreCase(url.toExternalForm())) {
                        setConcept(null);
                        progressDialog.setVisible(false);
                        AppFrameDispatcher.showErrorDialog("A media with the URL, '" + url.toExternalForm() + "', was already found in '" + concept.getPrimaryConceptNameAsString() + "'.");
                        return;
                    }
                }
                progressBar.setString("Building data");
                progressBar.setValue(1);
                final Media media = new Media();
                media.setUrl(url.toExternalForm());
                media.setCaption(p.getCaptionArea().getText());
                media.setCredit(p.getCreditArea().getText());
                final String type = (String) p.getTypeComboBox().getSelectedItem();
                media.setType(type);
                boolean isPrimary = p.getPrimaryCheckBox().isSelected();
                media.setPrimary(isPrimary);
                if (isPrimary) {
                    final Media primaryMedia = concept.getPrimaryMedia(type);
                    if (primaryMedia != null) {
                        log.info("You are adding a primary media of '" + media.getUrl() + "' to " + concept.getPrimaryConceptNameAsString() + ". This concept contained a primary media of '" + primaryMedia.getUrl() + "' which is now set to a secondary media");
                        primaryMedia.setPrimary(false);
                    }
                }
                concept.addMedia(media);
                IHistory history = HistoryFactory.add((UserAccount) KnowledgebaseApp.DISPATCHER_USERACCOUNT.getValueObject(), media);
                concept.addHistory(history);
                progressBar.setString("Updating database");
                progressBar.setValue(2);
                try {
                    ConceptDAO.getInstance().update(concept);
                    final UserAccount userAccount = (UserAccount) KnowledgebaseApp.DISPATCHER_USERACCOUNT.getValueObject();
                    if (userAccount.isAdmin()) {
                        ApproveHistoryTask.approve(userAccount, history);
                    }
                } catch (DAOException e1) {
                    concept.removeMedia(media);
                    concept.removeHistory(history);
                    final String s = "Failed to upate '" + concept.getPrimaryConceptNameAsString() + "' in the database. Removing the media reference to '" + url.toExternalForm() + "'.";
                    log.error(s, e1);
                    AppFrameDispatcher.showErrorDialog(s);
                }
                progressBar.setString("Refreshing");
                progressBar.setValue(3);
            }
            setConcept(null);
            progressDialog.setVisible(false);
            ((KnowledgebaseApp) KnowledgebaseApp.DISPATCHER.getValueObject()).getKnowledgebaseFrame().refreshTreeAndOpenNode(concept.getPrimaryConceptNameAsString());
        }
