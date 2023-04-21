    public static int outputImage(ProgressMonitorDialog monitor, GraphicalViewer viewer, String saveFilePath) throws InterruptedException {
        int format = getFormatType(saveFilePath);
        if (format == -1) {
            Activator.showMessageDialog("dialog.message.export.image.not.supported");
            return -1;
        }
        Image img = null;
        try {
            img = createImage(viewer);
            ExportToImageWithProgressManager exportToImageManager = new ExportToImageWithProgressManager(img, format, saveFilePath);
            monitor.run(true, true, exportToImageManager);
            Exception exception = exportToImageManager.getException();
            if (exception != null) {
                throw exception;
            }
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            if (e.getCause() instanceof OutOfMemoryError) {
                Activator.showMessageDialog("dialog.message.export.image.out.of.memory");
            } else {
                Activator.showExceptionDialog(e);
            }
            return -1;
        } finally {
            if (img != null) {
                img.dispose();
            }
        }
        return format;
    }
