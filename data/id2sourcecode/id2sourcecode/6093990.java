    private static String saveLatestPic2() {
        try {
            URL url = new URL(website2);
            InputStream is = (url.openStream());
            Scanner sc = new Scanner(is);
            String img_path = website2 + getImagePath2(sc);
            System.out.println("ImagePath= " + img_path);
            String filename = img_path.split("/")[img_path.split("/").length - 1];
            System.out.println("Filename: " + filename);
            File loc = new File(pic_folder2 + filename);
            if (!loc.exists()) {
                loc.createNewFile();
                BufferedImage bi = ImageIO.read(new URL(img_path));
                double widfac = 1150. / bi.getWidth();
                double heifac = 700. / bi.getHeight();
                double fac = Math.min(1, Math.min(widfac, heifac));
                BufferedImage bic = new BufferedImage((int) (bi.getWidth() * fac), (int) (bi.getHeight() * fac), bi.getType());
                Graphics g = bic.getGraphics();
                g.drawImage(bi, 0, 0, (int) (bi.getWidth() * fac), (int) (bi.getHeight() * fac), null);
                ImageIO.write(bic, "png", ImageIO.createImageOutputStream(loc));
            }
            return loc.getAbsolutePath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
