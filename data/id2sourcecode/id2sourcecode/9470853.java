    @Override
    public byte[] getFxResource(String Resource) throws StandardException {
        try {
            String sFormDir = java.util.ResourceBundle.getBundle(getClass().getName()).getString("FormsDir");
            FileInputStream fi = new FileInputStream(sFormDir + File.separator + Resource);
            byte fileData[] = new byte[(int) fi.getChannel().size()];
            fi.close();
            fileData = ObjUtil.compress(fileData);
            return fileData;
        } catch (Exception e) {
            Logger.getLogger("fireteam").log(Level.SEVERE, null, e);
            throw (ObjUtil.throwStandardException(e));
        }
    }
