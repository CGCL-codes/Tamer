    public void editCondition(Condition condition) {
        SavedCondition savedCondition = null;
        for (SavedCondition current : conditions) {
            if (condition.equals(current.getCondition())) {
                savedCondition = current;
                if (logger.isDebugEnabled()) logger.debug("Found saved condition {}.", savedCondition);
                break;
            }
        }
        boolean adding = false;
        if (savedCondition == null) {
            adding = true;
            savedCondition = new SavedCondition(condition);
        }
        try {
            savedCondition = savedCondition.clone();
        } catch (CloneNotSupportedException e) {
            if (logger.isErrorEnabled()) logger.error("Couldn't clone saved condition!", e);
        }
        editConditionDialog.setSavedCondition(savedCondition);
        editConditionDialog.setAdding(adding);
        for (; ; ) {
            Windows.showWindow(editConditionDialog, preferencesDialog, true);
            if (editConditionDialog.isCanceled()) {
                break;
            }
            SavedCondition newCondition = editConditionDialog.getSavedCondition();
            String newName = newCondition.getName();
            Condition containedCondition = newCondition.getCondition();
            int conditionIndex = -1;
            int nameIndex = -1;
            for (int i = 0; i < conditions.size(); i++) {
                if (containedCondition.equals(conditions.get(i).getCondition())) {
                    conditionIndex = i;
                }
                if (newName.equals(conditions.get(i).getName())) {
                    nameIndex = i;
                }
            }
            if (logger.isDebugEnabled()) logger.debug("conditionIndex={}, nameIndex={}", conditionIndex, nameIndex);
            boolean done = false;
            if (nameIndex >= 0 && nameIndex != conditionIndex) {
                String dialogTitle = "Duplicate condition name!";
                String message = "A different confition with the same name does already exist!\nOverwrite that condition?";
                int result = JOptionPane.showConfirmDialog(this, message, dialogTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (JOptionPane.OK_OPTION == result) {
                    conditionTableModel.set(nameIndex, savedCondition);
                    if (conditionIndex >= 0) {
                        conditionTableModel.remove(conditionIndex);
                        if (conditionIndex < nameIndex) {
                            nameIndex--;
                        }
                    }
                    conditionIndex = nameIndex;
                    done = true;
                }
            } else if (conditionIndex < 0) {
                conditionIndex = conditionTableModel.add(savedCondition);
                done = true;
            } else {
                conditionTableModel.set(conditionIndex, savedCondition);
                done = true;
            }
            if (done) {
                conditionTable.setRowSelectionInterval(conditionIndex, conditionIndex);
                updateConditions();
                break;
            }
        }
    }
