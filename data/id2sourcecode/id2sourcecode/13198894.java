    public void startDownload() {
        String pixivImageUrl = p.getProperty("pixiv_image_url");
        String pixivImageSize = p.getProperty("pixiv_image_size");
        String pixivImageInterval = p.getProperty("pixiv_image_interval");
        String pixivImageFolder = p.getProperty("pixiv_image_folder");
        System.out.println("pixiv_image_url = " + pixivImageUrl);
        System.out.println("pixiv_image_size = " + pixivImageSize);
        System.out.println("pixiv_image_interval = " + pixivImageInterval);
        System.out.println("pixiv_image_folder = " + pixivImageFolder);
        System.out.println("download start");
        String imagePath = pixivImageUrl + "?mode=" + pixivImageSize + "&illust_id=";
        String illustId = "18358117";
        try {
            URL url = new URL(imagePath + illustId);
            System.out.println("Locating image: " + url.toString());
            InputStream in = url.openStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(pixivImageFolder + illustId + ".jpg"));
            for (int b; (b = in.read()) != -1; ) {
                out.write(b);
            }
            out.close();
            in.close();
            System.out.println("Download sucess, illust_id = " + illustId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
