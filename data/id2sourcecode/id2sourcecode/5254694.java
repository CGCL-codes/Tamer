    private void provideBizDriver() {
        Path bizCompPath = new Path(model.getFilePath());
        IPath bizDriverPath = bizCompPath.removeFileExtension().addFileExtension("xdr");
        try {
            FileUtils.copyFile(ResourceUtils.getAbsolutePath(SQL_EXAMPLE_BIZDRIVER), bizDriverPath.toPortableString());
            model.getDocument().getRootElement().setAttribute(XAwareConstants.BIZCOMPONENT_ATTR_DRIVER, bizDriverPath.lastSegment(), XAwareConstants.xaNamespace);
            String newBizDriverRef = RichUIEditorXmlUtil.getBizDriverRef(model);
            bizDriverRef = newBizDriverRef;
            refChanged = true;
            usingSampleDatabase = true;
            try {
                if (info != null) {
                    info.closeJdbcTemplate();
                }
                info = new SQLBizDriverInfo();
                info.setOverwriteFile(true);
                info.setUseExisting(false);
                info.loadFromFile(bizDriverRef);
            } catch (JDOMException e1) {
                ControlFactory.showStackTrace(translator.getString("Error parsing bizdriver ") + bizDriverRef, e1);
            } catch (IOException e1) {
            }
            populateScreen();
        } catch (IOException e) {
            ControlFactory.showStackTrace(translator.getString("Unable to use the sample bizdriver"), e);
        }
    }
