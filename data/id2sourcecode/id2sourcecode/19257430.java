    public static OutputStream createDownload(HttpServletResponse response, String type, String zip, String filename, boolean savetodisk) throws IOException {
        if (zip == null || zip.equals("none")) {
            response.setContentType(type);
            if (savetodisk) {
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            }
            return response.getOutputStream();
        } else {
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + "." + zip);
            if (zip.equals("zip")) {
                response.setContentType("application/zip");
                ZipOutputStream zipout = new ZipOutputStream(response.getOutputStream());
                zipout.putNextEntry(new ZipEntry(filename));
                return zipout;
            } else if (zip.equals("gzip")) {
                return new GZIPOutputStream(response.getOutputStream());
            } else {
                throw new IllegalArgumentException("Invalid zip method");
            }
        }
    }
