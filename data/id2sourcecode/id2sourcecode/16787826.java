    public static boolean fromURL(URL url, MainClass mc) {
        String[] info;
        TextField TF = mc.TF;
        PixCanvas canvas = mc.canvas;
        java.awt.Image[] images;
        TF.setText(url.toString());
        DataInputStream in;
        File file;
        try {
            URLConnection u = url.openConnection();
            in = new DataInputStream(u.getInputStream());
            int size = u.getContentLength();
            byte[] array = new byte[size];
            int bytes_read = 0;
            while (bytes_read < size) {
                bytes_read += in.read(array, bytes_read, size - bytes_read);
            }
            in.close();
            DicomReader dR = new DicomReader(array);
            images = dR.getImages();
            info = dR.getInfos();
        } catch (IOException e) {
            tools.Tools.debug("exception " + e);
            TF.setText("Error file format not recognized");
            return false;
        }
        MediaTracker tr = new MediaTracker(canvas);
        System.out.println("Images found " + images.length);
        for (int i = 0; i < images.length; i++) {
            tr.addImage(images[i], i);
            try {
                tr.waitForID(i);
            } catch (InterruptedException e) {
            }
            ;
            if (tr.isErrorID(i)) {
                TF.setText("Error while loading file...  try again");
                Tools.debug(tr.getErrorsAny().toString());
                return false;
            }
            System.out.println("Adding image " + i);
            if (images[i] == null) {
                System.out.println("Image is null!");
            }
            PixObject po = new PixObject(url, images[i], canvas, true, info);
            mc.vimages.addElement(po);
            po.isDicom = true;
            canvas.repaint();
        }
        return true;
    }
