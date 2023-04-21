    public static boolean takeScreenShot(String url, String fileLoc) throws Exception {
        log.debug("Starting XVFB");
        process = Runtime.getRuntime().exec(XVFB_COMMAND);
        log.debug("Creating firefox binary");
        FirefoxBinary firefox = new FirefoxBinary();
        firefox.setEnvironmentProperty("DISPLAY", ":" + DISPLAY_NUMBER);
        driver = new FirefoxDriver(firefox, null);
        log.debug("Get URL: " + url);
        driver.get(url);
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        log.debug("Writing image to file: " + new File(fileLoc).getAbsolutePath());
        FileUtils.copyFile(scrFile, new File(fileLoc));
        driver.close();
        process.destroy();
        driver = null;
        process = null;
        return true;
    }
