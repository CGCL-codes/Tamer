    public void saveProjectAs() {
        setStatus("Please wait...");
        int answer = JOptionPane.OK_OPTION;
        String projectName;
        String[] existingProjects;
        try {
            existingProjects = RepositoryFactory.getProjectNames();
        } catch (VerinecException e) {
            logger.throwing(getClass().getName(), "saveAsNetwork", e);
            JOptionPane.showMessageDialog(this, "Could not get existing project names\n" + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        while ((projectName = JOptionPane.showInputDialog(this, "Enter Project Name:")) != null) {
            if (projectName.length() == 0) continue;
            for (int i = 0; i < existingProjects.length; i++) {
                if (projectName.equals(existingProjects[i])) {
                    answer = JOptionPane.showConfirmDialog(this, "The project '" + projectName + "' exists already\nYes to overwrite, no to enter other name, or cancel.");
                    if (answer == JOptionPane.CANCEL_OPTION) return;
                    logger.fine("Erasing project to overwrite: " + projectName);
                    try {
                        repo = RepositoryFactory.createRepository(projectName);
                        repo.drop();
                        repo = null;
                    } catch (Throwable e) {
                        logger.throwing(getClass().getName(), "saveAsNetwork", e);
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
            if (answer == JOptionPane.OK_OPTION) {
                setTitle(title + " " + projectName);
                try {
                    repo = RepositoryFactory.createRepository(projectName);
                } catch (Throwable e) {
                    logger.throwing(getClass().getName(), "saveAsNetwork", e);
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
                }
                saveProject();
                break;
            }
        }
    }
