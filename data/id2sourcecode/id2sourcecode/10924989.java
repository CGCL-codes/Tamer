    public byte[] getAndSendScreenshot(Robot r, boolean colorImage) {
        r.delay(200);
        int imageColorType;
        if (colorImage) {
            count = 1;
            firstTimeGray = true;
            imageColorType = BufferedImage.TYPE_INT_RGB;
        } else {
            imageColorType = BufferedImage.TYPE_BYTE_GRAY;
            if (firstTimeGray) {
                firstTimeGray = false;
                System.out.println("Trying to create Grayscale image");
            } else {
                count++;
                System.out.println("Trying to divide the grayscale image");
            }
        }
        if (!isFitScreen) {
            imageColorType = BufferedImage.TYPE_INT_RGB;
            Image image = r.createScreenCapture(new Rectangle(viewPortX, viewPortY, deviceWidth + ((zoomIn * deviceWidth) - (zoomOut * deviceWidth)), deviceHeight + ((zoomIn * deviceHeight) - (zoomOut * deviceHeight))));
            bufferedImage = new BufferedImage(deviceWidth, deviceHeight, imageColorType);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, deviceWidth, deviceHeight);
            graphics.drawImage(image, 0, 0, deviceWidth, deviceHeight, null);
            image = bufferedImage.getScaledInstance(deviceWidth, deviceHeight, imageColorType);
            bufferedImage = new BufferedImage(deviceWidth, deviceHeight, imageColorType);
            graphics = bufferedImage.getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, deviceWidth, deviceHeight);
            graphics.drawImage(image, 0, 0, deviceWidth, deviceHeight, null);
        } else {
            float aspectRatio = 1;
            aspectRatio = (float) (screenWidth / screenHeight);
            int fitScreenWidth = deviceWidth;
            int fitScreenHeight = deviceHeight;
            if (deviceHeight > deviceWidth) {
                fitScreenWidth = deviceWidth;
                fitScreenHeight = (int) (deviceWidth / aspectRatio);
            }
            imageColorType = BufferedImage.TYPE_BYTE_GRAY;
            colorImage = false;
            Image image = r.createScreenCapture(new Rectangle(0, 0, screenWidth, screenHeight));
            bufferedImage = new BufferedImage(fitScreenWidth, fitScreenHeight, imageColorType);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, deviceWidth, deviceHeight);
            graphics.drawImage(image, 0, 0, deviceWidth, (int) (fitScreenHeight / count), null);
            image = bufferedImage.getScaledInstance(screenWidth, (int) (screenHeight / count), imageColorType);
            bufferedImage = new BufferedImage(deviceWidth, (int) (fitScreenHeight), imageColorType);
            graphics = bufferedImage.getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, deviceWidth, (int) (deviceHeight));
            graphics.drawImage(image, 0, 0, deviceWidth, (int) (fitScreenHeight / count), null);
        }
        File file = new File("mobiledesktopserver.jpg");
        try {
            file.createNewFile();
            FileOutputStream fileout = new FileOutputStream(file);
            ImageIO.write(bufferedImage, "jpg", fileout);
            fileout.flush();
            fileout.close();
            FileInputStream filein = new FileInputStream(file);
            imageBuffer = new BufferedInputStream(filein);
            screenshot = new byte[(int) file.length()];
            imageBuffer.read(screenshot, 0, screenshot.length);
        } catch (FileNotFoundException ex) {
            System.out.println("\nError related to file");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error in sending screenshot byte array");
            ex.printStackTrace();
        }
        if (file.length() < 15000) {
            return screenshot;
        } else {
            if (isFitScreen) {
                return getAndSendScreenshot(r, false);
            } else {
                if (colorImage) return getAndSendScreenshot(r, false); else return screenshot;
            }
        }
    }
