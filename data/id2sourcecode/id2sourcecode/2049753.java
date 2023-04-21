    public String createLogin(String pwd, String displayname, String email) throws SQLException {
        if (email.trim().length() == 0 || pwd.trim().length() == 0 || displayname.length() == 0) {
            return "Please enter all the required details.";
        } else if (dbs.getResultSet("SELECT * FROM Logins WHERE Login = " + SQLFuncs.s2sql(email)).next()) {
            return "Sorry, one of those details has been taken.";
        } else if (dbs.getResultSet("SELECT * FROM Logins WHERE DisplayName = " + SQLFuncs.s2sql(displayname)).next()) {
            return "Sorry, one of those details has been taken.";
        } else if (displayname.indexOf("@") >= 0) {
            return "Sorry, please do not have an '@' in your name.";
        }
        int id = dbs.RunIdentityInsert_Syncd("INSERT INTO Logins (Pwd, LoginCode, DisplayName, Login, TotalTurns, TotalDaysTakingTurns, Location, AboutMe, TotalConcedes, EmailForumReplies, Website, EmailOnTurn) VALUES (" + SQLFuncs.s2sql(pwd.trim()) + ", " + System.currentTimeMillis() + ", " + SQLFuncs.s2sql(displayname) + ", " + SQLFuncs.s2sql(email) + ", 0, 0, '', '', 0, 1, '', 1)");
        try {
            WebsiteEventsTable.AddRec(dbs, " New login '" + displayname + "' has been registered.", -1);
        } catch (Exception ex) {
            DSRWebServer.HandleError(ex, true);
        }
        try {
            URL url = new URL("http://127.0.0.3:8081/importusers.php");
            Scanner in = new Scanner(url.openStream());
            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println(line);
            }
            in.close();
        } catch (Exception ex) {
            DSRWebServer.HandleError(ex, true);
        }
        this.selectRow(id);
        return "";
    }
