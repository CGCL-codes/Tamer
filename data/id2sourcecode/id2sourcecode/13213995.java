    protected static ImagePlus transferFromVTK(final vtkImageData image) throws VTKException {
        try {
            return VTKUtil.createImagePlus(image);
        } catch (IOException e) {
            throw new VTKException("Error transferring image from VTK. " + e.getMessage(), e);
        } catch (VtkImageException e) {
            throw new VTKException("Error transferring image from VTK. " + e.getMessage(), e);
        }
    }
