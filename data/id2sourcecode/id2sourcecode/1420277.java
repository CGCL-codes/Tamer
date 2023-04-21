    private MenuController() {
        super();
        try {
            prop.load(url.openStream());
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        loggedUserFlag = Integer.valueOf(LoginController.getLoggedUser().getFlag());
        if (loggedUserFlag == UserConstants.ADMIN_FLAG) {
            prjDelAllowed = true;
            fileDelAllowed = true;
            versDelAllowed = true;
        }
    }
