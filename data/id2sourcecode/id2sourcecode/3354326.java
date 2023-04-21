    protected void startPageModule(BufferedWriter buf, String pageTitle) throws IOException {
        buf.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + c_LF);
        buf.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" >" + c_LF);
        buf.write("<head>" + c_LF);
        buf.write(t(1) + "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" + c_LF);
        buf.write(t(1) + "<title>" + pageTitle + "</title>" + c_LF);
        buf.write(t(1) + "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"css/style.css\" TITLE=\"Style\"/>" + c_LF);
        buf.write(t(1) + "<script src=\"" + UnitTH.JS_DIR + "/" + UnitTH.SORT_SCRIPT + "\" type=\"text/javascript\"></script>" + c_LF);
        buf.write(t(1) + "<script type=\"text/javascript\">" + c_LF);
        buf.write(t(2) + "function sortTables() {" + c_LF);
        buf.write(t(3) + "var runTable = new TableSort;" + c_LF);
        buf.write(t(3) + "var testCaseTable = new TableSort;" + c_LF);
        buf.write(t(3) + "var spreadTable = new TableSort;" + c_LF);
        buf.write(t(3) + "runTable.init(\"run_table\");" + c_LF);
        buf.write(t(3) + "testCaseTable.init(\"testcase_table\");" + c_LF);
        buf.write(t(3) + "spreadTable.init(\"spread_table\");" + c_LF);
        buf.write(t(2) + "}" + c_LF);
        buf.write(t(2) + "window.onload = sortTables;" + c_LF);
        buf.write(t(1) + "</script>" + c_LF);
        buf.write("</head>" + c_LF);
    }
