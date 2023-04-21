    private void checkOverwriteProject(String input) throws IOException {
        LOG.debug("Checkout whether the user wants to overwrite existing project");
        String overwrite = null;
        boolean goOn = true;
        System.out.println("\nThe project already exists!");
        do {
            System.out.println("Do you really want to overwrite an existing " + "application? [Yes] [No]");
            overwrite = projectIn.readLine().toLowerCase();
            if (overwrite.equals("yes") || overwrite.equals("y")) {
                project.setProjectName(input);
                goOn = false;
                ;
            } else if (overwrite.equals("no") || overwrite.equals("n")) {
                setProjectName();
                goOn = false;
            }
        } while (goOn);
    }
