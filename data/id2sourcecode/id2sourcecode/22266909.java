    public static void main(String[] args) {
        if (args.length < 2) {
            printUsageMessage();
            System.exit(1);
        }
        String outputFileName = null;
        File inputFile = new File(args[0]);
        PlanarImage image = JAI.create("ImageRead", inputFile);
        String arg1 = args[1];
        String fileExtension = null;
        if (arg1.equals(JPEG2000) || arg1.equals(JPEG_2000)) fileExtension = "jp2"; else if (arg1.equals(JPEG_LOSSLESS) || arg1.equals(JPEG_LS)) fileExtension = "jls"; else if (arg1.equals(JPEG)) fileExtension = "jpg"; else if (arg1.equals(TIFF)) fileExtension = "tif"; else fileExtension = arg1;
        outputFileName = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().indexOf(".")) + "." + fileExtension;
        File outputFile = new File(outputFileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            ImageIO.write(image, arg1, out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                image.dispose();
                out.flush();
                out.close();
                FileInputStream fis = new FileInputStream(outputFile);
                if (fis.getChannel().size() > 1) {
                    Logger.getLogger(ImageConverter.class.getName()).log(Level.INFO, "Conversion Done!");
                } else {
                    Logger.getLogger(ImageConverter.class.getName()).log(Level.WARNING, "Conversion problem encountered, no joy on " + outputFile.getName());
                }
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
