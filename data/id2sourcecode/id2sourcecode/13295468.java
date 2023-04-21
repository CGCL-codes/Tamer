    public boolean applyNewEditor(String userid, int cid, String affiliation, String url1, String title1, String description1, int[] languages1, String url2, String title2, String description2, int[] languages2, String url1web, String url2web, int[] locations1, int[] locations2) {
        DBConnection con = null;
        boolean result = false;
        int subid = 0;
        DirectoryUtil dUtil = new DirectoryUtil();
        affiliation = dUtil.clean(affiliation);
        if (affiliation.length() >= 255) affiliation = affiliation.substring(0, 255);
        url1 = dUtil.clean(url1);
        url1web = dUtil.clean(url1web);
        title1 = dUtil.clean(title1);
        if (title1.length() >= 40) title1 = title1.substring(0, 40);
        description1 = dUtil.clean(description1);
        if (description1.length() >= 255) description1 = description1.substring(0, 255);
        url2 = dUtil.clean(url2);
        url2web = dUtil.clean(url2web);
        title2 = dUtil.clean(title2);
        if (title2.length() >= 40) title2 = title2.substring(0, 40);
        description2 = dUtil.clean(description2);
        if (description2.length() >= 255) description2 = description2.substring(0, 255);
        DirectoryCategoryRequest dcr = new DirectoryCategoryRequest();
        int catToSubmit = dcr.findCategoryWithEditor(cid);
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            StringBuffer query = new StringBuffer();
            con.executeUpdate("INSERT INTO mdir_Submission (di_submission_date, di_submission_email, di_submission_url, di_submission_name, di_submission_description, di_submission_language_id, di_submission_grade, di_submission_url2, di_submission_name2, di_submission_description2, di_submission_language_id2, di_submission_grade2, di_submission_cid, di_submission_affiliation, di_submission_type, di_submission_category_sent, di_submission_date_sent, di_submission_url_web, di_submission_url2_web) VALUES (getdate(), \'" + userid + "\', \'" + url1 + "\', \'" + title1 + "\',\'" + description1 + "\',0,5,\'" + url2 + "\', \'" + title2 + "\',\'" + description2 + "\',0,5," + cid + ",\'" + affiliation + "\',1," + catToSubmit + ",getDate(),'" + url1web + "','" + url2web + "')");
            OID newoid = DBUtil.getCurrentOID(con, "mdir_Submission");
            con.executeUpdate("INSERT INTO mdir_Submission_Category (di_submitcat_cid, di_submitcat_subid) VALUES (" + catToSubmit + "," + newoid.toString() + ")");
            for (int i = 0; i < languages1.length; i++) {
                con.executeUpdate("INSERT INTO mdir_Submission_Language1 (di_submit_language1_subid, di_submit_language1_languageid) VALUES ( " + newoid.toString() + ", " + languages1[i] + ")");
            }
            for (int i = 0; i < languages2.length; i++) {
                con.executeUpdate("INSERT INTO mdir_Submission_Language2 (di_submit_language2_subid, di_submit_language2_languageid) VALUES ( " + newoid.toString() + ", " + languages2[i] + ")");
            }
            for (int i = 0; i < locations1.length; i++) {
                con.executeUpdate("INSERT INTO mdir_Submission_Location1 (di_submit_location1_subid, di_submit_location1_locationid) VALUES ( " + newoid.toString() + ", " + locations1[i] + ")");
            }
            for (int i = 0; i < locations2.length; i++) {
                con.executeUpdate("INSERT INTO mdir_Submission_Location2 (di_submit_location2_subid, di_submit_location2_locationid) VALUES ( " + newoid.toString() + ", " + locations2[i] + ")");
            }
            if (vOtherUrl1 != null) {
                for (Enumeration e = vOtherUrl1.elements(); e.hasMoreElements(); ) {
                    ObjectOtherURL oUrl = (ObjectOtherURL) e.nextElement();
                    String url_value = oUrl.getUrlValue();
                    int url_type = oUrl.getUrlType();
                    con.executeUpdate("INSERT INTO mdir_Submission_Url1 (di_submit_URL1_subid, di_submit_URL1_typeid, di_submit_URL1_value) VALUES ( " + newoid.toString() + "," + url_type + ",'" + url_value + "') ");
                }
            }
            if (vOtherUrl2 != null) {
                for (Enumeration e = vOtherUrl2.elements(); e.hasMoreElements(); ) {
                    ObjectOtherURL oUrl = (ObjectOtherURL) e.nextElement();
                    String url_value = oUrl.getUrlValue();
                    int url_type = oUrl.getUrlType();
                    con.executeUpdate("INSERT INTO mdir_Submission_Url2 (di_submit_URL2_subid, di_submit_URL2_typeid, di_submit_URL2_value) VALUES ( " + newoid.toString() + "," + url_type + ",'" + url_value + "') ");
                }
            }
            con.commit();
            result = true;
        } catch (SQLException sqle) {
            System.err.println("EXCEPTION:applyNewEditor:" + sqle);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.reset();
                } catch (SQLException e) {
                }
                con.release();
            }
        }
        return result;
    }
