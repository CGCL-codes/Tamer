    void menuOpenURL() {
        animate = false;
        resetScaleCombos();
        TextPrompter textPrompter = new TextPrompter(shell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
        textPrompter.setText(bundle.getString("OpenURLDialog"));
        textPrompter.setMessage(bundle.getString("EnterURL"));
        String urlname = textPrompter.open();
        if (urlname == null) return;
        Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
        shell.setCursor(waitCursor);
        imageCanvas.setCursor(waitCursor);
        try {
            URL url = new URL(urlname);
            InputStream stream = url.openStream();
            loader = new ImageLoader();
            if (incremental) {
                loader.addImageLoaderListener(new ImageLoaderListener() {

                    public void imageDataLoaded(ImageLoaderEvent event) {
                        incrementalDataLoaded(event);
                    }
                });
                incrementalThreadStart();
            }
            long startTime = System.currentTimeMillis();
            imageDataArray = loader.load(stream);
            stream.close();
            loadTime = System.currentTimeMillis() - startTime;
            if (imageDataArray.length > 0) {
                currentName = urlname;
                fileName = null;
                previousButton.setEnabled(imageDataArray.length > 1);
                nextButton.setEnabled(imageDataArray.length > 1);
                animateButton.setEnabled(imageDataArray.length > 1 && loader.logicalScreenWidth > 0 && loader.logicalScreenHeight > 0);
                imageDataIndex = 0;
                displayImage(imageDataArray[imageDataIndex]);
                resetScrollBars();
            }
        } catch (Exception e) {
            showErrorDialog(bundle.getString("Loading_lc"), urlname, e);
        } finally {
            shell.setCursor(null);
            imageCanvas.setCursor(crossCursor);
            waitCursor.dispose();
        }
    }
