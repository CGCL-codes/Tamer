        public void run() {
            try {
                final URL url = new URL(urlString);
                final URLConnection connection = url.openConnection();
                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                final ImageInputStream stream = ImageIO.createImageInputStream(in);
                final Iterator readers = ImageIO.getImageReaders(stream);
                final ImageReader reader = (ImageReader) readers.next();
                IIOReadProgressListener progressListener = new IIOReadProgressListener() {

                    public void sequenceStarted(ImageReader imageReader, int i) {
                    }

                    public void sequenceComplete(ImageReader imageReader) {
                    }

                    public void imageStarted(ImageReader imageReader, int i) {
                    }

                    public void imageProgress(ImageReader imageReader, float v) {
                    }

                    public void imageComplete(ImageReader imageReader) {
                        System.out.println("   complete " + url);
                    }

                    public void thumbnailStarted(ImageReader imageReader, int i, int i1) {
                    }

                    public void thumbnailProgress(ImageReader imageReader, float v) {
                    }

                    public void thumbnailComplete(ImageReader imageReader) {
                    }

                    public void readAborted(ImageReader imageReader) {
                        System.out.println("ABORT " + url);
                    }
                };
                reader.addIIOReadProgressListener(progressListener);
                reader.setInput(stream, true);
                BufferedImage img = reader.read(0);
                addImageIcon(img);
            } catch (IOException e) {
                System.err.println("Can't load image URL " + urlString + ": " + e.getMessage());
            }
        }
