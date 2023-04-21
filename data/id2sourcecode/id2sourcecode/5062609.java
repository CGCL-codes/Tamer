    private static int loadPicture(InputStream fis, HSSFWorkbook wb) throws IOException {
        int pictureIndex;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            int c;
            while ((c = fis.read()) != -1) bos.write(c);
            pictureIndex = wb.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
        } finally {
            if (fis != null) fis.close();
            if (bos != null) bos.close();
        }
        return pictureIndex;
    }
