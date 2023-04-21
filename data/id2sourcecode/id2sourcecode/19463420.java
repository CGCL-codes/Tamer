    public void generate() {
        try {
            createFile(logoFile, new StringBuffer("<html><a href=\"http://www.webperformancegroup.com\" target=\"_blank\"><img src=\"wpg-logo.gif\" alt=\"Web Performance Group\" align=\"center\" border=\"0\" /></a></html>"));
            DataInputStream di = new DataInputStream(this.getClass().getResourceAsStream("/com.ununbium/wpg-logo.gif"));
            FileOutputStream fo = new FileOutputStream(directory + "/" + "wpg-logo.gif");
            byte[] b = new byte[1];
            while (di.read(b, 0, 1) != -1) fo.write(b, 0, 1);
            di.close();
            fo.close();
            indexSB.append("<html><head><title>Test Report for " + scriptName + "</title></head>");
            indexSB.append("<frameset cols=\"20%,80%\">" + "<frameset rows=\"20%,80%\">" + "<frame src=\"" + logoFile + "\" noresize>" + "<frame src=\"" + leftMenuFile + "\" name=\"leftmenu\" noresize>" + "</frameset>" + "<frame src=\"" + resultFile + "\" name=\"main\" scrolling=\"yes\">" + "</frameset> <noframes>" + "<h1>Your Browser Does Not Support Frames.</h1>" + "</noframes></html>");
            createFile(indexFile, indexSB);
            leftMenuSB.append("<html><body><a href=\"" + resultFile + "\" target=\"main\">Script Run Results</a><br>" + "<a href=\"" + logFile + "\" target=\"_blank\">View Output Log</a><br>" + "<p><h2>Transactions:</h2> <ul>");
            for (int i = 0; i < stats.size(); i++) {
                ScriptStatus sso = (ScriptStatus) stats.get(i);
                leftMenuSB.append("<li> <a href=\"trans-" + sso.tranName + ".html\" target=\"main\">" + sso.tranName + "</a> </li>");
                processStatObject(sso);
            }
            leftMenuSB.append("</ul>");
            leftMenuSB.append("</body></html>");
            createFile(leftMenuFile, leftMenuSB);
            processResultFile();
        } catch (Exception e) {
            System.out.println("Exception in TestReport::generate: " + e);
            e.printStackTrace();
        }
    }
