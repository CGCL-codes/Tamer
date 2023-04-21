    @Override
    public void doSave(final IProgressMonitor monitor) {
        Display display = this.getGraphicalViewer().getControl().getDisplay();
        display.syncExec(new Runnable() {

            @Override
            public void run() {
                String xml = XMLSerializer.serialize(chart);
                try {
                    InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                    IFile file = (IFile) getEditorInput().getAdapter(IFile.class);
                    file.setContents(in, false, true, monitor);
                    getCommandStack().markSaveLocation();
                } catch (UnsupportedEncodingException e) {
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        });
    }
