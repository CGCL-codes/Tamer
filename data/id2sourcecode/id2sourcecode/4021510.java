    @Override
    public HomeRecorder getHomeRecorder() {
        if (this.homeRecorder == null) {
            URL codeBase = this.applet.getCodeBase();
            final String writeHomeURL = getAppletParameter(this.applet, WRITE_HOME_URL_PARAMETER, "writeHome.php");
            final String readHomeURL = getAppletParameter(this.applet, READ_HOME_URL_PARAMETER, "readHome.php?home=%s");
            final String listHomesURL = getAppletParameter(this.applet, LIST_HOMES_URL_PARAMETER, "listHomes.php");
            this.homeRecorder = new HomeAppletRecorder(getURLStringWithCodeBase(codeBase, writeHomeURL), getURLStringWithCodeBase(codeBase, readHomeURL), getURLStringWithCodeBase(codeBase, listHomesURL));
        }
        return this.homeRecorder;
    }
