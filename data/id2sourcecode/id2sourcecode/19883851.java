    void menuReopen() {
        if (currentName == null) return;
        animate = false;
        resetScrollBars();
        resetScaleCombos();
        Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
        shell.setCursor(waitCursor);
        imageCanvas.setCursor(waitCursor);
        try {
            loader = new ImageLoader();
            long startTime = System.currentTimeMillis();
            ImageData[] newImageData;
            if (fileName == null) {
                URL url = new URL(currentName);
                InputStream stream = url.openStream();
                newImageData = loader.load(stream);
                stream.close();
            } else {
                newImageData = loader.load(fileName);
            }
            loadTime = System.currentTimeMillis() - startTime;
            imageDataIndex = 0;
            displayImage(newImageData[imageDataIndex]);
        } catch (Exception e) {
            showErrorDialog(bundle.getString("Reloading_lc"), currentName, e);
        } finally {
            shell.setCursor(null);
            imageCanvas.setCursor(crossCursor);
            waitCursor.dispose();
        }
    }
