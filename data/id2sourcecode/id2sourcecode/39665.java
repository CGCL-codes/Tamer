    public static void writeImage(File image, File mp3) {
        MediaFile oMediaFile = new MP3File(mp3);
        ID3V2_3_0Tag tag = null;
        try {
            tag = (ID3V2_3_0Tag) oMediaFile.getID3V2Tag();
        } catch (Exception e) {
        } finally {
            if (tag == null) {
                tag = new ID3V2_3_0Tag();
            }
        }
        try {
            InputStream s = new FileInputStream(image);
            FileInputStream in = new FileInputStream(image);
            FileChannel fc = in.getChannel();
            byte[] data = new byte[(int) fc.size()];
            ByteBuffer bb = ByteBuffer.wrap(data);
            fc.read(bb);
            String imageType = image.getName().substring(image.getName().lastIndexOf(".") + 1);
            String mimeType = imageType.equalsIgnoreCase("jpg") ? "image/jpeg" : "image/" + imageType;
            APICID3V2Frame picFrame = new APICID3V2Frame(mimeType, APICID3V2Frame.PictureType.FrontCover, "Frontcover", data);
            tag.addAPICFrame(picFrame);
            oMediaFile.setID3Tag(tag);
            oMediaFile.sync();
            System.out.println("Tag written");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
