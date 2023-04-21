    public void downLoadPic(String picurl, String hashcode) {
        URL url;
        BufferedInputStream in;
        FileOutputStream file;
        try {
            System.out.println("Start download picture!");
            String fileName = hashcode + ".jpg";
            String filePath = cfg.getBrandPath() + "pictures\\";
            url = new URL(picurl);
            in = new BufferedInputStream(url.openStream());
            file = new FileOutputStream(new File(filePath + fileName));
            int t;
            while ((t = in.read()) != -1) {
                file.write(t);
            }
            file.close();
            in.close();
            System.out.println("Download picture successed!\r\n" + "path : " + filePath + fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.err.println("create filed failed! check the current path valid.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("get page picture failed...");
            e.printStackTrace();
        }
    }
