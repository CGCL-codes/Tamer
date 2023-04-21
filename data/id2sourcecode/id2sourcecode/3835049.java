    public void testReadWriteRdf() throws Exception {
        File jpeg = new File(System.getProperty("project.root"), "build/test/exif-rdf.jpg");
        Model model = getPhotoModel();
        ByteArrayOutputStream rdf = new ByteArrayOutputStream();
        model.write(rdf);
        log.debug("RDF to write to jpeg:\n" + rdf);
        ByteArrayOutputStream jpegOS = new ByteArrayOutputStream();
        JpegCommentWriter jcw = new JpegCommentWriter(jpegOS, new FileInputStream(jpeg));
        jcw.write(rdf.toString());
        jcw.close();
        FileOutputStream fos = new FileOutputStream(jpeg);
        fos.write(jpegOS.toByteArray());
        fos.close();
        FileInputStream jpegIn = new FileInputStream(jpeg);
        JpegHeaders jh = new JpegHeaders(jpegIn, new org.w3c.tools.jpeg.Exif());
        String comments = StringUtils.join(jh.getComments(), "");
        jpegIn.close();
        assertNotNull(comments);
        log.info("EXIF comments from jpeg:\n" + comments);
        Model fromJpeg = ModelFactory.createDefaultModel();
        fromJpeg.read(new StringReader(comments), "");
        fromJpeg.write(System.out);
        log.info("RDF from EXIF comments:\n" + comments);
    }
