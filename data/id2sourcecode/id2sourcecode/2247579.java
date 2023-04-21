    private void handleImageInsert(File imgFile) {
        if (!this.finished && new FileNameExtensionFilter("", "jpg", "png", "gif", "jpeg").accept(imgFile)) {
            final String imageAddedMessage = KendoResources.getString(KendoResources.UI_TOOL_IMAGE_INSERTED_MESSAGE);
            final String variant = Settings.getProperty("language.variant");
            ImageData imgData = this.event.getSystemState().getImageData();
            ImageGallery gallery = imgData.getImageGallery();
            try {
                FileChannel channel = new RandomAccessFile(imgFile, "r").getChannel();
                FileLock lock = channel.lock(0, 0, true);
                File imported = gallery.importImage(imgFile);
                imgData.setImageFilename(variant, imported.getName());
                lock.release();
                channel.close();
                this.ui.closeModalDialog();
                this.poller.cancel();
                this.finished = true;
                this.ui.showMessage(imageAddedMessage + " " + this.event.getSystemState());
                this.ui.updateUi(this.event.getSource());
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
