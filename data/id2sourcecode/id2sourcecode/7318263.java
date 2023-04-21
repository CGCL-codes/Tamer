    @Override
    protected void save(IEditorPart editorPart, GraphicalViewer viewer, String saveFilePath) throws Exception {
        ERDiagram diagram = this.getDiagram();
        Category currentCategory = diagram.getCurrentCategory();
        int currentCategoryIndex = diagram.getCurrentCategoryIndex();
        try {
            ProgressMonitorDialog monitor = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
            boolean outputImage = true;
            File dir = new File(saveFilePath);
            FileUtils.deleteDirectory(dir);
            dir = new File(saveFilePath + "/image");
            dir.mkdirs();
            String outputImageFilePath = saveFilePath + "/image/er.png";
            if (outputImage) {
                diagram.setCurrentCategory(null, 0);
                int imageFormat = ExportToImageAction.outputImage(monitor, viewer, outputImageFilePath);
                if (imageFormat == -1) {
                    throw new InputException(null);
                }
            }
            Map<TableView, Location> tableLocationMap = getTableLocationMap(this.getGraphicalViewer(), this.getDiagram());
            ExportToHtmlWithProgressManager manager = new ExportToHtmlWithProgressManager(saveFilePath, diagram, tableLocationMap);
            monitor.run(true, true, manager);
            if (manager.getException() != null) {
                throw manager.getException();
            }
        } catch (IOException e) {
            Activator.showMessageDialog(e.getMessage());
        } catch (InterruptedException e) {
        } catch (Exception e) {
            Activator.showExceptionDialog(e);
        } finally {
            diagram.setCurrentCategory(currentCategory, currentCategoryIndex);
        }
        this.refreshProject();
    }
