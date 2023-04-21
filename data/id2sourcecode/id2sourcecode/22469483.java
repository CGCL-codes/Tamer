    public void load(URL url) {
        try {
            properties.load(url.openStream());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error reading existing bsgraphics.properties", e);
        }
        isPBuffer = getProperty("ch.blackspirit.graphics.jogl.debug.pbuffer", isPBuffer);
        isDebugGL = getProperty("ch.blackspirit.graphics.jogl.debug.debuggl", isDebugGL);
        isTraceGL = getProperty("ch.blackspirit.graphics.jogl.debug.trace", isTraceGL);
        try {
            traceLogLevel = Level.parse(getProperty("ch.blackspirit.graphics.jogl.debug.trace.log.level", traceLogLevel.getName()));
        } catch (Throwable t) {
        }
        imageDrawingWidth = getProperty(Properties.MAX_IMAGE_DRAWING_WIDTH, imageDrawingWidth);
        imageDrawingHeight = getProperty(Properties.MAX_IMAGE_DRAWING_HEIGHT, imageDrawingHeight);
    }
