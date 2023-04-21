    public void writeJpegComment(File jpeg, String rdf) throws IOException {
        ByteArrayOutputStream jpegOS = new ByteArrayOutputStream();
        JpegCommentWriter jcw = new JpegCommentWriter(jpegOS, new FileInputStream(jpeg));
        try {
            jcw.write(rdf);
        } finally {
            jcw.close();
        }
        FileOutputStream fos = new FileOutputStream(jpeg);
        try {
            fos.write(jpegOS.toByteArray());
        } finally {
            fos.close();
        }
    }
