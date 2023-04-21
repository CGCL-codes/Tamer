    private boolean approveOverwrite(String fileName) {
        String message = "File: " + fileName + " already exists.\n" + "Are you sure that you want to overwrite its contents?";
        NotifyDescriptor.Confirmation conf = new NotifyDescriptor.Confirmation(message);
        return DialogDisplayer.getDefault().notify(conf) == NotifyDescriptor.YES_OPTION;
    }
