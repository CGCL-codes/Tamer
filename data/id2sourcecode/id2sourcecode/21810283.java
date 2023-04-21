    public static void testWriteResource() {
        try {
            start = System.currentTimeMillis();
            String mimeType = reader.getCdxInstance().getMimeTypeforIdentifier(identifier);
            String ext = new MimeTypeMapper().getExtension(mimeType);
            String resource = reader.stripExtension(arcFile, ".arc") + ext;
            reader.writeResource(identifier, resource);
            long writeResouceTime = (System.currentTimeMillis() - start);
            assertTrue(new File(resource).length() > 0);
            System.out.println("Resource to Disc Total Time: " + writeResouceTime + " ms");
        } catch (ARCException e) {
            e.printStackTrace();
        }
    }
