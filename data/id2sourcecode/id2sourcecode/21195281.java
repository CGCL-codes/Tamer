    private static BufferedImage combineTwoImages(BufferedImage inputImage1, BufferedImage inputImage2, int method) throws IOException {
        BufferedImage outputImage = new BufferedImage(inputImage1.getWidth(), inputImage1.getHeight(), inputImage1.getType());
        WritableRaster outputRaster = outputImage.getRaster();
        Raster inputRaster1 = inputImage1.getData();
        Raster inputRaster2 = inputImage2.getData();
        if (inputImage1.getWidth() != inputImage2.getWidth() || inputImage1.getHeight() != inputImage2.getHeight()) throw new IOException("Images must be same size to combine.");
        if (inputRaster1.getNumBands() != inputRaster2.getNumBands()) throw new IOException("# bands in image1 must = # bands in image 2.");
        for (int i = 0; i < inputImage1.getHeight(); i++) {
            for (int j = 0; j < inputImage1.getWidth(); j++) {
                for (int b = 0; b < inputRaster1.getNumBands(); b++) {
                    int outPixel = 0;
                    int pixel1 = inputRaster1.getSample(j, i, b);
                    int pixel2 = inputRaster2.getSample(j, i, b);
                    switch(method) {
                        case 0:
                            if (pixel1 < pixel2) outPixel = pixel1; else outPixel = pixel2;
                            break;
                        case 1:
                            if (pixel1 > pixel2) outPixel = pixel1; else outPixel = pixel2;
                            break;
                        case 2:
                            outPixel = pixel1 & pixel2;
                            break;
                        case 3:
                            outPixel = pixel1 | pixel2;
                            break;
                        case 4:
                            outPixel = pixel1 ^ pixel2;
                            break;
                        case 5:
                            outPixel = (pixel1 + pixel2) / 2;
                            break;
                        case 6:
                            outPixel = Math.abs(pixel1 - pixel2);
                            break;
                    }
                    outputRaster.setSample(j, i, b, outPixel);
                }
            }
        }
        return outputImage;
    }
