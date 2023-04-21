    public static void getAndsaveImages(String[] imageUrl) throws IOException {
        byte[] b;
        int length;
        URL url;
        InputStream is;
        OutputStream os;
        String destinationFile;
        String fileName;
        File file;
        for (int i = 0; i < imageUrl.length; i++) {
            fileName = imageUrl[i] + ".jpg";
            file = new File(fileName);
            if (file.exists() == false) {
                destinationFile = "images//UsersProfilePics//" + GraphPlotter.quserList[i] + ".jpg";
                url = new URL(imageUrl[i]);
                try {
                    is = url.openStream();
                    os = new FileOutputStream(destinationFile);
                    b = new byte[4096];
                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    is.close();
                    os.close();
                } catch (IOException e) {
                    System.out.println("***Exception***");
                    FileInputStream fn = new FileInputStream("images//UsersProfilePics//" + defaultPics[randomNumber()]);
                    os = new FileOutputStream(destinationFile);
                    b = new byte[4096];
                    while ((length = fn.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    fn.close();
                    os.close();
                }
                System.out.println(GraphPlotter.quserList[i] + "...." + Integer.toString(i));
            }
        }
    }
