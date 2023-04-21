    private void handleLoginEvent(ViewEvent event) {
        String action = event.getArg1();
        LoginView loginView = (LoginView) event.getInitiator();
        if (action.equals("ok")) {
            String driverClass = loginView.getDriverClass();
            String url = loginView.getUrl();
            String userName = loginView.getUserName();
            String password = loginView.getPassword();
            loginView.closeDialog();
            openConnection(driverClass, url, userName, password);
        } else if (action.equals("cancel")) {
            loginView.closeDialog();
        }
    }
