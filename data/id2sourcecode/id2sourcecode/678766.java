    ImageData(String filename) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            final File f = new File(filename);
            final DigestInputStream is = new DigestInputStream(new FileInputStream(f), md);
            bImg = ImageIO.read(is);
            key = is.getMessageDigest().digest();
            bb = ByteBuffer.wrap(key);
            fullName = filename;
            name = f.getName();
            path = f.getParent();
            zoomFactor = 0;
            is.close();
        } catch (IOException e) {
            System.out.println(e);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
    }
