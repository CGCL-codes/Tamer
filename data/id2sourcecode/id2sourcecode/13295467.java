    public boolean submitNewSite(String username, int cid, String siteURL, String siteName, String siteDesc, int[] siteLanguages, String siteURLweb, int[] siteLocations) {
        DBConnection con = null;
        int rs = 0;
        int subid = 0;
        DirectoryUtil dUtil = new DirectoryUtil();
        boolean result = false;
        siteURL = dUtil.clean(siteURL);
        siteURLweb = dUtil.clean(siteURLweb);
        siteName = dUtil.clean(siteName);
        if (siteName.length() >= 40) siteName = siteName.substring(0, 40);
        if (siteDesc == null) {
            siteDesc = "";
        }
        siteDesc = dUtil.clean(siteDesc);
        if (siteDesc.length() >= 255) siteDesc = siteDesc.substring(0, 255);
        if (siteURL.trim().equals("http://")) siteURL = "";
        if (siteURLweb.trim().equals("http://")) siteURLweb = "";
        if (siteURL.trim().equals(siteURLweb.trim()) && siteURL.trim().equals("")) return result;
        if ((siteName.trim().equals("")) || siteLanguages.length <= 0) return result;
        DirectoryCategoryRequest dcr = new DirectoryCategoryRequest();
        int catToSubmit = dcr.findCategoryWithEditor(cid);
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            StringBuffer query = new StringBuffer();
            con.executeUpdate("INSERT INTO mdir_Submission (di_submission_date, di_submission_email, di_submission_url, di_submission_name, di_submission_description, di_submission_language_id, di_submission_grade, di_submission_url2, di_submission_name2, di_submission_description2, di_submission_language_id2, di_submission_grade2, di_submission_cid, di_submission_affiliation, di_submission_type, di_submission_category_sent, di_submission_date_sent, di_submission_url_web, di_submission_url2_web) VALUES (getdate(), \'" + username + "\', \'" + siteURL + "\', \'" + siteName + "\',\'" + siteDesc + "\',0,0,'','','',0,0," + cid + ",'',0," + catToSubmit + ",getdate(),'" + siteURLweb + "','')");
            OID newoid = DBUtil.getCurrentOID(con, "mdir_Submission");
            con.executeUpdate("INSERT INTO mdir_Submission_Category (di_submitcat_cid, di_submitcat_subid) VALUES (" + catToSubmit + "," + newoid.toString() + ")");
            for (int i = 0; i < siteLanguages.length; i++) {
                con.executeUpdate("INSERT INTO mdir_Submission_Language1 (di_submit_language1_subid, di_submit_language1_languageid) VALUES ( " + newoid.toString() + ", " + siteLanguages[i] + ") ");
            }
            for (int i = 0; i < siteLocations.length; i++) {
                con.executeUpdate("INSERT INTO mdir_Submission_Location1 (di_submit_location1_subid, di_submit_location1_locationid) VALUES ( " + newoid.toString() + ", " + siteLocations[i] + ") ");
            }
            if (vOtherUrl1 != null) {
                for (Enumeration e = vOtherUrl1.elements(); e.hasMoreElements(); ) {
                    ObjectOtherURL oUrl = (ObjectOtherURL) e.nextElement();
                    String url_value = oUrl.getUrlValue();
                    int url_type = oUrl.getUrlType();
                    con.executeUpdate("INSERT INTO mdir_Submission_Url1 (di_submit_URL1_subid, di_submit_URL1_typeid, di_submit_URL1_value) VALUES ( " + newoid.toString() + "," + url_type + ",'" + url_value + "') ");
                }
            }
            con.commit();
            result = true;
        } catch (SQLException sqle) {
            System.err.println("EXCEPTION:submitNewSite:" + sqle);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                }
            }
            result = false;
        } finally {
            try {
                con.reset();
            } catch (SQLException e) {
            }
            if (con != null) {
                con.release();
            }
        }
        return result;
    }
