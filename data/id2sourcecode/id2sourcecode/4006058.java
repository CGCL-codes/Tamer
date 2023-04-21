            @Override
            protected boolean performLeavingAction(WizardStepDirection direction) {
                reader.stopReading();
                reader.writeMetaDataInParameter();
                return true;
            }
