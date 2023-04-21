    public void download(String path, String filename) throws Exception {
        File f = new File(path);
        if (f.exists()) {
            try {
                response.setContentType("application/octet-stream;");
                response.setContentLength((int) f.length());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(filename.getBytes("KSC5601"), "8859_1") + "\"");
                byte[] bbuf = new byte[2048];
                BufferedInputStream fin = new BufferedInputStream(new FileInputStream(f));
                BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
                int read = 0;
                while ((read = fin.read(bbuf)) != -1) {
                    outs.write(bbuf, 0, read);
                }
                outs.close();
                fin.close();
            } catch (Exception e) {
                errorLog("{Malgn.download} " + e.getMessage());
                response.setContentType("text/html");
                out.println("File Download Error : " + e.getMessage());
            }
        } else {
            response.setContentType("text/html");
            out.println("File Not Found : " + path);
        }
    }
