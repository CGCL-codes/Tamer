    private void addFileDataSourceQueryChoosers(JUMPReader reader, JUMPWriter writer, final String description, WorkbenchContext workbenchContext, Class readerWriterDataSourceClass) {
        DataSourceQueryChooserManager.get(workbenchContext.getBlackboard()).addLoadDataSourceQueryChooser(new LoadFileDataSourceQueryChooser(readerWriterDataSourceClass, description, extensions(readerWriterDataSourceClass), workbenchContext) {

            protected void addFileFilters(JFileChooser chooser) {
                super.addFileFilters(chooser);
                InstallStandardDataSourceQueryChoosersPlugIn.addCompressedFileFilter(description, chooser);
            }
        }).addSaveDataSourceQueryChooser(new SaveDxfFileDataSourceQueryChooser(readerWriterDataSourceClass, description, extensions(readerWriterDataSourceClass), workbenchContext));
    }
