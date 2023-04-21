    private void addFileDataSourceQueryChoosers(JUMPReader reader, JUMPWriter writer, final String description, WorkbenchContext context, Class readerWriterDataSourceClass) {
        DataSourceQueryChooserManager chooserManager = DataSourceQueryChooserManager.get(context.getBlackboard());
        chooserManager.addLoadDataSourceQueryChooser(new LoadFileDataSourceQueryChooser(readerWriterDataSourceClass, description, extensions(readerWriterDataSourceClass), context) {

            protected void addFileFilters(JFileChooser chooser) {
                super.addFileFilters(chooser);
                InstallStandardDataSourceQueryChoosersPlugIn.addCompressedFileFilter(description, chooser);
            }
        });
        chooserManager.addSaveDataSourceQueryChooser(new SaveFileDataSourceQueryChooser(readerWriterDataSourceClass, description, extensions(readerWriterDataSourceClass), context));
    }
