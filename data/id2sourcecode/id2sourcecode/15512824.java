    private void addFileDataSourceQueryChoosers(JUMPReader reader, JUMPWriter writer, final String description, WorkbenchContext context, Class readerWriterDataSourceClass) {
        DataSourceQueryChooserManager.get(context.getBlackboard()).addSaveDataSourceQueryChooser(new SaveFileDataSourceQueryChooser(readerWriterDataSourceClass, description, extensions(readerWriterDataSourceClass), context));
    }
