    public void setQuota(String username, String quota) throws Exception {
        if (!quota.equals("")) quota = (Long.parseLong(quota) * 1024 * 1024) + "";
        Properties permissions = (Properties) NMCommon.readXMLObject("/Library/Application Support/NotMac/CrushFTP/users/127.0.0.1_53818/" + username + "/VFS.XML");
        String data = permissions.getProperty(("/" + username + "/").toUpperCase(), "(read)(write)(resume)");
        if (data.indexOf("(quota") < 0) data += "(quota104857600)";
        String original_data = data;
        data = data.substring(data.indexOf("(quota") + 6, data.indexOf(")", data.indexOf("(quota")));
        data = NMCommon.replace_str(original_data, data, quota + "");
        permissions.put(("/" + username + "/").toUpperCase(), data);
        common_code.writeXMLObject("/Library/Application Support/NotMac/CrushFTP/users/127.0.0.1_53818/" + username + "/VFS.XML", permissions, "VFS");
    }
