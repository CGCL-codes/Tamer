    public static void showOrHideDefaultPasswordWarnings() {
        boolean adminNotDefaultPassword = MandatoryUser.ADMIN.hasChangedDefaultPasswordOrDisabled();
        boolean userNotDefaultPassword = MandatoryUser.USER.hasChangedDefaultPasswordOrDisabled();
        boolean wsreaderNotDefaultPassword = MandatoryUser.WSREADER.hasChangedDefaultPasswordOrDisabled();
        boolean wswriterNotDefaultPassword = MandatoryUser.WSWRITER.hasChangedDefaultPasswordOrDisabled();
        Clients.evalJavaScript("showOrHideDefaultPasswordWarnings(" + adminNotDefaultPassword + ", " + userNotDefaultPassword + ", " + wsreaderNotDefaultPassword + ", " + wswriterNotDefaultPassword + ");");
    }
