    public InputStream disseminate(Context context, Item item) throws DSpaceSwordException, SwordError, SwordServerException {
        try {
            String tempDir = ConfigurationManager.getProperty("upload.temp.dir");
            String fn = tempDir + File.separator + "SWORD." + item.getID() + "." + UUID.randomUUID().toString() + ".zip";
            OutputStream outStream = new FileOutputStream(new File(fn));
            ZipOutputStream zip = new ZipOutputStream(outStream);
            Bundle[] originals = item.getBundles("ORIGINAL");
            for (Bundle original : originals) {
                Bitstream[] bss = original.getBitstreams();
                for (Bitstream bitstream : bss) {
                    ZipEntry ze = new ZipEntry(bitstream.getName());
                    zip.putNextEntry(ze);
                    InputStream is = bitstream.retrieve();
                    Utils.copy(is, zip);
                    zip.closeEntry();
                    is.close();
                }
            }
            zip.close();
            return new TempFileInputStream(new File(fn));
        } catch (SQLException e) {
            throw new DSpaceSwordException(e);
        } catch (IOException e) {
            throw new DSpaceSwordException(e);
        } catch (AuthorizeException e) {
            throw new DSpaceSwordException(e);
        }
    }
