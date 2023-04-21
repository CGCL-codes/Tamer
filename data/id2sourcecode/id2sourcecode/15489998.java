    public static boolean handleOutputFileAlreadyExists(Component pFrame, String pOutputFileName) {
        boolean proceed = true;
        SimpleTextArea textArea = new SimpleTextArea("The output file you selected already exists:\n" + pOutputFileName + "\nThis operation will overwrite this file.\nAre you sure you want to proceed?");
        JOptionPane messageOptionPane = new JOptionPane();
        messageOptionPane.setMessage(textArea);
        messageOptionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        messageOptionPane.setOptionType(JOptionPane.YES_NO_OPTION);
        messageOptionPane.createDialog(pFrame, "Overwrite existing file?");
        Integer response = (Integer) messageOptionPane.getValue();
        if (null != response && response.intValue() == JOptionPane.YES_OPTION) {
        } else {
            proceed = false;
        }
        return (proceed);
    }
