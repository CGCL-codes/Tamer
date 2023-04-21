    private void writeMap() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userConfigFile));
            oos.writeObject(this.userMap);
            oos.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            logger.error("UserAuthorizationManager::writeMap: Could not find the file : " + userConfigFile.toString() + " Message : " + ex.getMessage(), ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("UserAuthorizationManager::writeMap: Could not read content from the file : " + userConfigFile.toString() + " Message : " + ex.getMessage(), ex);
        }
    }
