    public CSVImportWizard(String i18nKey, ConfigurationListener listener, final File preselectedFile, final boolean showStoreInRepositoryStep, final RepositoryLocation preselectedLocation, Object... i18nArgs) {
        super(i18nKey, i18nArgs);
        file = preselectedFile;
        if (listener != null) {
            reader = (CSVDataReader) listener;
        } else {
            try {
                reader = OperatorService.createOperator(com.rapidminer.operator.io.CSVDataReader.class);
            } catch (OperatorCreationException e) {
                e.printStackTrace();
            }
        }
        parametersBackup = (Parameters) reader.getParameters().clone();
        addStep(new FileSelectionWizardStep(this, new SimpleFileFilter("CSV File (.csv)", ".csv")) {

            @Override
            protected boolean performEnteringAction(WizardStepDirection direction) {
                if (file != null && file.exists()) {
                    this.fileChooser.setSelectedFile(file);
                }
                return true;
            }

            @Override
            protected boolean performLeavingAction(WizardStepDirection direction) {
                file = getSelectedFile();
                File oldFile = null;
                try {
                    oldFile = reader.getParameterAsFile(CSVDataReader.PARAMETER_CSV_FILE);
                } catch (UndefinedParameterError e) {
                    oldFile = null;
                }
                if (oldFile == null || !oldFile.equals(file)) {
                    reader.clearAllReaderSettings();
                }
                reader.setParameter(CSVDataReader.PARAMETER_CSV_FILE, file.getAbsolutePath());
                return true;
            }
        });
        addStep(new ParseFileWizardStep("specify_csv_parsing_options", reader) {

            @Override
            protected boolean performEnteringAction(WizardStepDirection direction) {
                reader.stopReading();
                if (reader.attributeNamesDefinedByUser()) {
                    reader.loadMetaDataFromParameters();
                    List<Object[]> dummyData = new LinkedList<Object[]>();
                    setData(dummyData);
                    new ProgressThread("load_csv_file") {

                        @Override
                        public void run() {
                            final List<Object[]> data;
                            try {
                                data = reader.getPreviewAsList(getProgressListener(), true);
                            } catch (OperatorException e) {
                                SwingTools.showVerySimpleErrorMessage(e.getMessage(), e);
                                return;
                            }
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    setData(data);
                                }
                            });
                        }
                    }.start();
                } else {
                    settingsChanged();
                }
                return true;
            }

            @Override
            protected boolean performLeavingAction(WizardStepDirection direction) {
                reader.stopReading();
                return true;
            }

            @Override
            protected void settingsChanged() {
                reader.clearAllReaderSettings();
                reader.setParameter(Encoding.PARAMETER_ENCODING, getEncoding().displayName());
                reader.setParameter(CSVDataReader.PARAMETER_TRIM_LINES, Boolean.toString(trimLines()));
                reader.setParameter(CSVDataReader.PARAMETER_SKIP_COMMENTS, Boolean.toString(skipComments()));
                reader.setParameter(CSVDataReader.PARAMETER_COMMENT_CHARS, getCommentCharacters());
                reader.setParameter(CSVDataReader.PARAMETER_USE_FIRST_ROW_AS_ATTRIBUTE_NAMES, Boolean.toString(getUseFirstRowAsColumnNames()));
                reader.setParameter(CSVDataReader.PARAMETER_USE_QUOTES, Boolean.toString(useQuotes()));
                reader.setParameter(CSVDataReader.PARAMETER_QUOTES_CHARACTER, Character.toString(getQuotesCharacter()));
                reader.setParameter(CSVDataReader.PARAMETER_ESCAPE_CHARACTER, Character.toString(getEscapeCharacter()));
                reader.setParameter(CSVDataReader.PARAMETER_COLUMN_SEPARATORS, getSplitExpression());
                List<Object[]> dummyData = new LinkedList<Object[]>();
                setData(dummyData);
                new ProgressThread("load_csv_file") {

                    @Override
                    public void run() {
                        final List<Object[]> data;
                        try {
                            data = reader.getPreviewAsList(getProgressListener(), true);
                        } catch (OperatorException e) {
                            SwingTools.showVerySimpleErrorMessage(e.getMessage(), e);
                            return;
                        }
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                setData(data);
                            }
                        });
                    }
                }.start();
            }

            @Override
            protected boolean canGoBack() {
                return true;
            }

            @Override
            protected boolean canProceed() {
                return true;
            }
        });
        addStep(new ParseValueTypesWizardStep("value_type_selection", reader) {

            @Override
            protected boolean canGoBack() {
                return true;
            }

            @Override
            protected boolean canProceed() {
                return true;
            }

            @Override
            protected boolean performEnteringAction(WizardStepDirection direction) {
                if (reader.attributeNamesDefinedByUser()) {
                    reader.loadMetaDataFromParameters();
                }
                super.performEnteringAction(direction);
                return true;
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
                    return transferData(reader, getRepositoryLocation());
                }
            });
        }
        layoutDefault(HUGE);
    }
