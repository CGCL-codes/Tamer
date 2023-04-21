    public boolean updateSite(String username, int sid, String siteURL, String siteName, String siteDesc, int[] siteLanguages, String siteURLweb, int[] siteLocations) {
        DBConnection con = null;
        int rs = 0;
        boolean result = false;
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            StringBuffer query = new StringBuffer();
            query.append("UPDATE mdir_Site SET di_site_url=\'" + siteURL + "\',di_site_name");
            query.append("=\'" + XMLUtil.encodeNumerical(siteName) + "\',di_site_description=\'" + XMLUtil.encodeNumerical(siteDesc) + "\',di_site_updated_date");
            query.append("=getdate(),di_site_updated_by=\'" + username + "\',");
            query.append("di_site_shadowname=\'" + XMLUtil.shadow(StringUtil.replace(siteName, "\'", "\'\'")) + "\',di_site_shadowdescription");
            query.append("=\'" + XMLUtil.shadow(StringUtil.replace(siteDesc, "\'", "\'\'")) + "\',di_site_weburl='" + StringUtil.replace(siteURLweb, "\'", "\'\'") + "' ");
            query.append("WHERE di_site_id=" + sid);
            rs = con.executeUpdate(query.toString());
            result = (rs != 0);
            rs = con.executeUpdate("DELETE FROM mdir_Site_Language WHERE di_site_language_siteid=" + sid);
            for (int i = 0; i < siteLanguages.length; i++) {
                rs = con.executeUpdate("INSERT INTO mdir_Site_Language (di_site_language_siteid, di_site_language_languageid)  VALUES (" + sid + "," + siteLanguages[i] + ")");
                result = result && (rs != 0);
            }
            rs = con.executeUpdate("DELETE FROM mdir_Site_Location WHERE di_site_location_siteid=" + sid);
            for (int i = 0; i < siteLocations.length; i++) {
                rs = con.executeUpdate("INSERT INTO mdir_Site_Location (di_site_location_siteid, di_site_location_locationid) VALUES (" + sid + "," + siteLocations[i] + ")");
                result = result && (rs != 0);
            }
            rs = con.executeUpdate("DELETE FROM mdir_Url_Support WHERE di_otherurl_siteid=" + sid);
            if (vOtherUrl != null && vOtherUrl.size() > 0) {
                query = new StringBuffer();
                for (Enumeration e = vOtherUrl.elements(); e.hasMoreElements(); ) {
                    ObjectOtherURL oUrl = (ObjectOtherURL) e.nextElement();
                    String url_value = oUrl.getUrlValue();
                    int url_type = oUrl.getUrlType();
                    query.append("INSERT INTO mdir_Url_Support (di_otherurl_siteid, di_otherurl_typeid, di_otherurl_value) VALUES (" + sid + "," + url_type + ",'" + url_value + "') ");
                }
                rs = con.executeUpdate(query.toString());
            }
            con.commit();
        } catch (SQLException e) {
            System.out.println("EXCEPTION:");
            System.out.println(e.toString());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException re) {
                    System.out.println("EXCEPTION:");
                    System.out.println(re.toString());
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
