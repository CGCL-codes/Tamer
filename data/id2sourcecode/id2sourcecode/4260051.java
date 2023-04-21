    public void save(JRuntimeDesignerController controller) throws LayoutDataProviderException {
        try {
            URLConnection urlCon = fileURL.openConnection();
            urlCon.setDoOutput(true);
            controller.save(fileURL.openConnection().getOutputStream());
        } catch (IOException ex) {
            throw new LayoutDataProviderException(ex.getMessage(), ex);
        } catch (XmlException ex) {
            throw new LayoutDataProviderException(ex.getMessage(), ex);
        }
    }
