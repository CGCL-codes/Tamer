    public void testImageStream() {
        Image image = p.searchPic("Pic1").getImg();
        File file = new File(p.searchPic("Pic1").getImg().getUri());
        File destination = new File("Pic.jpg");
        if (file.equals(destination)) return;
        boolean buffered = true;
        try {
            InputStream fis = image.getImage(buffered);
            OutputStream fos = new FileOutputStream(destination);
            fos = new BufferedOutputStream(new FileOutputStream(destination));
            try {
                long start = System.currentTimeMillis();
                if (buffered) {
                    final int size = 8192;
                    byte[] buffer = new byte[size];
                    int read;
                    while ((read = fis.read(buffer, 0, size)) != -1) {
                        fos.write(buffer, 0, read);
                    }
                } else {
                    int read;
                    while ((read = fis.read()) != -1) {
                        fos.write(read);
                    }
                }
                System.out.println("copy duration: " + (System.currentTimeMillis() - start));
            } catch (IOException ioe) {
            } finally {
                try {
                    if (fos != null) fos.close();
                    if (fis != null) fis.close();
                } catch (IOException e) {
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
