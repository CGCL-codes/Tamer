    public void update() throws VTKException {
        final vtkImageData inputImageData = transferToVTK(inputImage);
        inputCast.SetInput(inputImageData);
        filter.SetDimensionality(inputImage.getStackSize() == 1 ? 2 : 3);
        filter.Update();
        outputImage = transferFromVTK(filter.GetOutput());
    }
