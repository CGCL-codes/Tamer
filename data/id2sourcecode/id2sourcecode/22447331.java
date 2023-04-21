    public String writeURLToFile(URL url, String path, int minHeight, int minWidth) throws IOException {
        String filename = "";
        String tmpFilename = "";
        int index = 0;
        File imageFile = null;
        File renameFile = null;
        Metadata metadata = null;
        Integer width = null;
        Integer height = null;
        boolean foundWidth = false;
        boolean foundHeight = false;
        String widthString = "";
        String heightString = "";
        String newFilename = "";
        String oldFilename = null;
        int count = 0;
        int numImagesSaved = 0;
        if (url.getFile() == null || url.getFile().indexOf("/") == -1) {
            throw new IOException("Invalid link");
        }
        index = url.getFile().lastIndexOf("/");
        tmpFilename = url.getFile().substring(index);
        filename = path + tmpFilename;
        System.out.println("Process link " + url.toString() + " with filename: " + filename);
        oldFilename = new String(filename);
        while (true) {
            imageFile = new File(filename);
            if (imageFile.exists()) {
                newFilename = oldFilename.substring(0, oldFilename.lastIndexOf(".")) + count + oldFilename.substring(oldFilename.lastIndexOf("."));
                filename = new String(newFilename);
                if (count > 1000) {
                    break;
                }
                count++;
            } else {
                break;
            }
        }
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[16384];
        for (int read = 0; (read = in.read(buffer)) != -1; out.write(buffer, 0, read)) ;
        if (filename.indexOf("%") != -1) {
            imageFile = new File(filename);
            renameFile = new File(filename.replace("%", "_"));
            imageFile.renameTo(renameFile);
            filename = new String(renameFile.getAbsolutePath());
        }
        if (out.toByteArray().length > MIN_IMAGE_SIZE_IN_BYTES) {
            RandomAccessFile file = new RandomAccessFile(filename, "rw");
            file.write(out.toByteArray());
            file.close();
            imageFile = new File(filename);
            try {
                metadata = ImageMetadataReader.readMetadata(imageFile);
            } catch (ImageProcessingException ex) {
                System.out.println("Couldn't get meta data: " + ex);
                if (ex.toString().indexOf("not the correct format") != -1) {
                    imageFile.delete();
                    return "";
                }
                metadata = null;
            }
            if (metadata != null) {
                Iterator directories = metadata.getDirectoryIterator();
                while (directories.hasNext()) {
                    Directory directory = (Directory) directories.next();
                    Iterator tags = directory.getTagIterator();
                    while (tags.hasNext()) {
                        Tag tag = (Tag) tags.next();
                        try {
                            if (tag.getTagName().contains("Width")) {
                                try {
                                    widthString = tag.getDescription().substring(0, tag.getDescription().indexOf(" "));
                                } catch (Exception ex3) {
                                    System.out.println("Could not get width: " + ex3 + " for tag description: " + tag.getDescription());
                                    foundWidth = false;
                                }
                                try {
                                    width = new Integer(widthString);
                                    foundWidth = true;
                                } catch (NumberFormatException ex2) {
                                    foundWidth = false;
                                }
                            }
                            if (tag.getTagName().contains("Height")) {
                                try {
                                    heightString = tag.getDescription().substring(0, tag.getDescription().indexOf(" "));
                                } catch (Exception ex4) {
                                    System.out.println("Could not get height: " + ex4 + " for tag description: " + tag.getDescription());
                                    foundHeight = false;
                                }
                                try {
                                    height = new Integer(heightString);
                                    foundHeight = true;
                                } catch (NumberFormatException ex2) {
                                    foundHeight = false;
                                }
                            }
                            if (foundHeight && foundWidth) {
                                break;
                            }
                        } catch (MetadataException ex1) {
                            System.out.println("EX1: " + ex1);
                        }
                    }
                    if (foundHeight && foundWidth) {
                        break;
                    }
                }
                if (foundWidth == false || foundHeight == false) {
                    numImagesSaved++;
                    return filename;
                } else {
                    System.out.println("Have width: " + width.toString());
                    System.out.println("Have height: " + height.toString());
                    if (width.intValue() < minWidth && height.intValue() < minHeight) {
                        imageFile.delete();
                        return "";
                    }
                    if (height.intValue() < 165) {
                        imageFile.delete();
                        return "";
                    }
                    if (width.intValue() > (height.intValue() * 4)) {
                        imageFile.delete();
                        return "";
                    }
                }
            } else {
                numImagesSaved++;
                return filename;
            }
        } else {
            return "";
        }
        numImagesSaved++;
        return filename;
    }
