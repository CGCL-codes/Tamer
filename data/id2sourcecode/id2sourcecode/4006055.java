    public ExcelImportWizard(String i18nKey, ConfigurationListener listener, File preselectedFile, final boolean showStoreInRepositoryStep, RepositoryLocation preselectedLocation, Object... i18nArgs) {
        super(i18nKey, i18nArgs);
        file = preselectedFile;
        if (listener != null) {
            reader = (ExcelExampleSource) listener;
        } else {
            try {
                reader = OperatorService.createOperator(com.rapidminer.operator.io.ExcelExampleSource.class);
            } catch (OperatorCreationException e) {
                throw new RuntimeException("Failed to create excel reader: " + e, e);
            }
        }
        parametersBackup = (Parameters) reader.getParameters().clone();
        addStep(STEP_FILE_SELECTION);
        addStep(new ExcelWorkSheetSelection(reader));
        addStep(new MetaDataDeclerationWizardStep("select_attributes", reader) {

            @Override
            protected JComponent getComponent() {
                JPanel typeDetection = new JPanel(ButtonDialog.createGridLayout(1, 2));
                typeDetection.setBorder(ButtonDialog.createTitledBorder("Value Type Detection"));
                typeDetection.add(new JLabel("Guess the value types of all attributes"));
                typeDetection.add(guessingButtonsPanel);
                Component[] superComponents = super.getComponent().getComponents();
                JPanel upperPanel = new JPanel(new BorderLayout());
                upperPanel.add(typeDetection, BorderLayout.NORTH);
                upperPanel.add(superComponents[0], BorderLayout.CENTER);
                JPanel panel = new JPanel(new BorderLayout(0, ButtonDialog.GAP));
                panel.add(upperPanel, BorderLayout.NORTH);
                panel.add(superComponents[1], BorderLayout.CENTER);
                return panel;
            }

            @Override
            protected void doAfterEnteringAction() {
                reader.setAttributeNamesDefinedByUser(true);
                ((ExcelExampleSource) reader).skipNameAnnotationRow(true);
            }

            @Override
            protected boolean performLeavingAction(WizardStepDirection direction) {
                reader.stopReading();
                reader.writeMetaDataInParameter();
                return true;
            }
        });
        if (showStoreInRepositoryStep) {
            addStep(new RepositoryLocationSelectionWizardStep(this, preselectedLocation != null ? preselectedLocation.getAbsoluteLocation() : null) {

                @Override
                protected boolean performLeavingAction(WizardStepDirection direction) {
                    synchronized (reader) {
                        boolean flag = transferData(reader, getRepositoryLocation());
                        return flag;
                    }
                }
            });
        }
        layoutDefault(HUGE);
    }
