    public void update() throws VTKException {
        final vtkImageData inputImageData = transferToVTK(inputImage);
        inputCast.SetInput(inputImageData);
        filter.Update();
        outputImage = transferFromVTK(filter.GetOutput());
    }
