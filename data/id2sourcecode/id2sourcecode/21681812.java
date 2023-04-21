    public String toString() {
        String retVal = "##### Wikipage ##### /n" + "name: " + name + "/nnamespace: " + namespace + "/nmodified: " + modified.toString() + "/nlastAccessed: " + lastAccessed.toString() + "/ndeleted: " + deleted + "/nlocale: " + locale + "/npageType: " + pageType + "/nreadPermission: " + readPermission + "/nwritePermission: " + writePermission + "/nversion: " + version + "/nlatest: " + latest + "/npageLayout: " + pageLayout + "/nmainContent: " + mainContent + "/nrightContent: " + rightContent;
        return retVal;
    }
