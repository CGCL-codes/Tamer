    private static String saveLatestPic() {
        try {
            URL url = new URL(website);
            InputStream is = (url.openStream());
            Scanner sc = new Scanner(is);
            String[] t = getImagePath(sc);
            String img_path = t[0];
            String title = t[1];
            System.out.println("ImagePath= " + img_path);
            String filename = img_path.split("/")[img_path.split("/").length - 1];
            System.out.println("Filename: " + filename);
            File loc = new File(pic_folder + filename);
            if (!loc.exists()) {
                loc.createNewFile();
                BufferedImage bi = ImageIO.read(new URL(img_path));
                double widfac = 1150. / bi.getWidth();
                double heifac = 700. / bi.getHeight();
                double fac = Math.min(1, Math.min(widfac, heifac));
                BufferedImage bic = new BufferedImage((int) (bi.getWidth() * fac), (int) (bi.getHeight() * fac) + 40, bi.getType());
                Graphics g = bic.getGraphics();
                g.drawImage(bi, 0, 0, (int) (bi.getWidth() * fac), (int) (bi.getHeight() * fac), null);
                int x = ((int) (bi.getWidth() * fac) - title.length() * 6) / 2;
                g.drawString(title, x, (int) (bi.getHeight() * fac) + 20);
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
