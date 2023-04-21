    public void loadProperties() throws IOException {
        File file = new File(filename);
        URL url = file.toURI().toURL();
        Properties temp = new Properties();
        temp.load(url.openStream());
        if (temp.getProperty("OriginOfImageCoordinatesX") != null) try {
            originofimagecoordinates.x = Double.valueOf(temp.getProperty("OriginOfImageCoordinatesX"));
        } catch (Exception e) {
            System.out.println("Error loading OriginOfImageCoordinatesX - leaving as default: " + e);
        }
        if (temp.getProperty("OriginOfImageCoordinatesY") != null) try {
            originofimagecoordinates.y = Double.valueOf(temp.getProperty("OriginOfImageCoordinatesY"));
        } catch (Exception e) {
            System.out.println("Error loading OriginOfImageCoordinatesY - leaving as default: " + e);
        }
        if (temp.getProperty("SkipProcessing") != null) skipprocessing = temp.getProperty("SkipProcessing").equals("true");
        for (int i = 0; i < 3; i++) for (int j = 0; j < 4; j++) {
            try {
                WorldToImageTransform.set(i, j, Double.valueOf(temp.getProperty("WorldToImageTransformMatrixRow" + i + "Column" + j)));
            } catch (Exception e) {
                System.out.println("Error loading WorldToImageTransformMatrixRow" + i + "Column" + j + " - leaving as default: " + e);
            }
        }
    }
