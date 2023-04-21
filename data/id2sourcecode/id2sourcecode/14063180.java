    private String updateFile(String sID, String sVersion, String sExtension, File fp) {
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(fp);
            byte[] bFileData = new byte[(int) fi.getChannel().size()];
            fi.read(bFileData);
            HashMap par = new HashMap();
            par.put("ID", sID);
            par.put("VER", sVersion);
            par.put("EXT", sExtension);
            par.put("DATA", ObjUtil.compress(bFileData));
            Any param = ClientMain.getInstance().createAny();
            param.insert_Value(par);
            param = ClientMain.getInstance().sessionRequest("setDocFile", param);
            return param.extract_wstring();
        } catch (Exception ex) {
            Errors.showError(ex);
        } finally {
            try {
                fi.close();
            } catch (IOException ex) {
                Errors.showError(ex);
            }
        }
        return null;
    }
