    public File getFileUploadParam(String parameter) {
        Object param = request.get(parameter);
        if (param == null) return null;
        if (param instanceof Part) {
            Part part = (Part) param;
            try {
                File temp = File.createTempFile("exist", ".xml");
                temp.deleteOnExit();
                OutputStream os = new FileOutputStream(temp);
                InputStream is = part.getInputStream();
                byte[] data = new byte[1024];
                int read = 0;
                while ((read = is.read(data)) > -1) {
                    os.write(data, 0, read);
                }
                is.close();
                part.dispose();
                return temp;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
