    public boolean saveFileCheck(File checkMe) {
        if (checkMe.isDirectory()) {
            CBUtility.error(checkMe.toString() + " is a directory.", null);
            return false;
        } else if (checkMe.exists()) {
            int saveAnswer = JOptionPane.showConfirmDialog(owner, (checkMe.toString() + "\n " + CBIntText.get("This file already exists.\nDo you want to overwrite this file?")), "Question", JOptionPane.OK_CANCEL_OPTION);
            return (saveAnswer == JOptionPane.OK_OPTION);
        }
        return true;
    }
