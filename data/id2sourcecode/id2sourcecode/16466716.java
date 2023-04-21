    public boolean addNewSite(String username, int cid, String siteURL, String siteName, String siteDesc, int[] siteLanguages, String siteURLweb, int[] siteLocations) {
        DBConnection con = null;
        int rs = 0;
        boolean result = false;
        int siteId = 0;
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            con.executeUpdate("INSERT INTO mdir_Site (di_site_cid, di_site_url, di_site_name, di_site_description, di_site_deleted_date, di_site_deleted_by, di_site_updated_date, di_site_updated_by, di_site_approved_date, di_site_approved_by, di_site_language_id, di_site_grade, di_site_submitted_date, di_site_submitted_by, di_site_creation_date, di_site_rating, di_site_shadowname,di_site_shadowdescription, di_site_weburl) VALUES (" + cid + ",\'" + siteURL + "\',\'" + XMLUtil.encodeNumerical(siteName) + "\',\'" + XMLUtil.encodeNumerical(siteDesc) + "\',NULL,NULL,getdate(),\'" + username + "\',getdate(),\'" + username + "\',0,0,getdate(),\'" + username + "\',getdate(),0,\'" + XMLUtil.shadow(StringUtil.replace(siteName, "\'", "\'\'")) + "\',\'" + XMLUtil.shadow(StringUtil.replace(siteDesc, "\'", "\'\'")) + "\','" + StringUtil.replace(siteURLweb, "\'", "\'\'") + "')");
            OID newoid = DBUtil.getCurrentOID(con, "mdir_Site");
            for (int i = 0; i < siteLanguages.length; i++) con.executeUpdate("INSERT INTO mdir_Site_Language (di_site_language_siteid, di_site_language_languageid) VALUES ( " + newoid.toString() + ", " + siteLanguages[i] + ")");
            for (int i = 0; i < siteLocations.length; i++) con.executeUpdate("INSERT INTO mdir_Site_Location (di_site_location_siteid, di_site_location_locationid) VALUES ( " + newoid.toString() + ", " + siteLocations[i] + ")");
            if (vOtherUrl != null) {
                for (Enumeration e = vOtherUrl.elements(); e.hasMoreElements(); ) {
                    ObjectOtherURL oUrl = (ObjectOtherURL) e.nextElement();
                    String url_value = oUrl.getUrlValue();
                    int url_type = oUrl.getUrlType();
                    con.executeUpdate("INSERT INTO mdir_Url_Support  (di_otherurl_siteid, di_otherurl_typeid, di_otherurl_value) VALUES ( " + newoid.toString() + "," + url_type + ",'" + url_value + "') ");
                }
            }
            con.commit();
            result = true;
        } catch (SQLException sqle) {
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
