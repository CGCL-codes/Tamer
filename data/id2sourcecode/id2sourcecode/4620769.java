    private void saveImportedWorkflowRemote(boolean incompleteDefinition) {
        System.out.println("TMP saveImportedWorkflowRemote()  incompleteDefinition =" + incompleteDefinition);
        String returnStr = cmdRefresh();
        if (returnStr.equals("WORKFLOW_NOT_EXIST")) {
            saveImportedWorkflow(incompleteDefinition);
        } else if (returnStr.equals("RUNNING")) {
            System.out.println("saveImportedWorkflowRemote() called (save pressed - W_R)");
            int ret = JOptionPane.showConfirmDialog(parent.getFrame(), "This workflow is running. Do you want to save as different name?", "Confirmation", JOptionPane.YES_NO_OPTION);
            switch(ret) {
                case JOptionPane.YES_OPTION:
                    saveAsRemote(incompleteDefinition, true);
                    break;
            }
        } else if (returnStr.equals("NOT_RUNNING")) {
            Object[] options = { "Overwrite", "Save as", "Cancel" };
            int returnOption = JOptionPane.showOptionDialog(parent.getFrame(), "This workflow has already existed. Do you want to overwrite or save as it in different name?", "Confirmation", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch(returnOption) {
                case 0:
                    System.out.println("saveImportedWorkflowRemote() called (save pressed - W_N_R, owerwrite)");
                    saveRemote(incompleteDefinition, true);
                    break;
                case 1:
                    System.out.println("saveImportedWorkflowRemote() called (save pressed - W_N_R, save as)");
                    saveAsRemote(incompleteDefinition, true);
                    break;
                case 2:
                    break;
            }
        }
    }
