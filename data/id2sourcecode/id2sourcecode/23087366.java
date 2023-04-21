        @Override
        public void action() {
            cueScene.getChanges().set(command.getChannelChanges());
            cueStepsModel.fireTableCellUpdated(cueStepIndex, COLUMN_CUE_STEP_SCENE);
        }
