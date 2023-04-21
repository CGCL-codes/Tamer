    private void setInputStream(InputStream ins) throws IOException {
        MediaTracker mt = new MediaTracker(this);
        int bytes_read = 0;
        byte data[] = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((bytes_read = ins.read(data)) > 0) baos.write(data, 0, bytes_read);
        ins.close();
        image = getToolkit().createImage(baos.toByteArray());
        mt.addImage(image, 0);
        try {
            mt.waitForID(0);
            mt.waitForAll();
            if (mt.statusID(0, true) != MediaTracker.COMPLETE) {
                System.out.println("Error occured in image loading = " + mt.getErrorsID(0));
            }
        } catch (InterruptedException e) {
            throw new IOException("Error reading image data");
        }
        canvas.setImage(image);
        if (DEBUG) System.out.println("calling invalidate");
    }
