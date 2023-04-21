    private void openConnection(String driverClass, String url, String userName, String password) {
        connectionModel.setDriverClass(driverClass);
        connectionModel.setUrl(url);
        connectionModel.setUserName(userName);
        connectionModel.setPassword(password);
        openList.setStatus("Connecting to " + url);
        connectionModel.connect();
    }
