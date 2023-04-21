    public void run() {
        try {
            if (deviceNumber != null) {
                GraphicsDevice[] devices = SAWUsableGraphicalDeviceResolver.getRasterDevices();
                if (devices != null) {
                    deviceNumber = Math.min(deviceNumber - 1, devices.length - 1);
                    screenshotProvider.setGraphicsDevice(devices[deviceNumber]);
                }
            } else {
                screenshotProvider.resetGraphicsDevice();
            }
            if (!screenshotProvider.isScreenCaptureInitialized() && !screenshotProvider.initializeScreenCapture()) {
                synchronized (this) {
                    try {
                        connection.getResultWriter().write("\nSAW>SAWSCREENSHOT:Screen capture cannot start on server!\nSAW>");
                        connection.getResultWriter().flush();
                    } catch (Exception e) {
                    }
                    finished = true;
                    return;
                }
            }
            connection.getResultWriter().write("\nSAW>SAWSCREENSHOT:Trying screen capture...\nSAW>");
            connection.getResultWriter().flush();
            clock.setTime(Calendar.getInstance().getTime());
            photoFile = new File(session.getWorkingDirectory(), "[" + clock.get(GregorianCalendar.YEAR) + "-" + dateTimeFormat.format(clock.getTime()) + ".png");
            photoOutputStream = new BufferedOutputStream(Channels.newOutputStream(new FileOutputStream(photoFile).getChannel()));
            BufferedImage screenCapture = screenshotProvider.createScreenCapture(drawPointer);
            ImageIO.write(screenCapture, "png", photoOutputStream);
            photoOutputStream.flush();
            synchronized (this) {
                connection.getResultWriter().write("\nSAW>SAWSCREENSHOT:Screen capture saved in:\nSAW>SAWSCREENSHOT:" + photoFile.getAbsolutePath() + "\nSAW>");
                connection.getResultWriter().flush();
                finished = true;
            }
        } catch (Exception e) {
            synchronized (this) {
                try {
                    connection.getResultWriter().write("\nSAW>SAWSCREENSHOT:Screen capture failed!\nSAW>");
                    connection.getResultWriter().flush();
                } catch (Exception e1) {
                }
                finished = true;
            }
        }
        if (photoOutputStream != null) {
            try {
                photoOutputStream.close();
            } catch (IOException e) {
            }
        }
        finished = true;
    }
