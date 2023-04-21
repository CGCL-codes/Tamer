    private void outputStreamFile(String filePath, HttpServletResponse response) throws Exception {
        OutputStream outs = response.getOutputStream();
        if (resourcePool.containsKey(filePath)) {
            String str = resourcePool.get(filePath).toString();
            byte[] bytes = str.getBytes("ISO8859-1");
            outs.write(bytes, 0, str.length());
        } else {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            byte[] bytes = new byte[1024];
            byte[] tempBytes = null;
            StringBuffer str = new StringBuffer();
            int read = 0;
            while ((read = inputStream.read(bytes)) >= 0) {
                outs.write(bytes, 0, read);
                tempBytes = new byte[read];
                for (int i = 0; i < read; i++) {
                    tempBytes[i] = bytes[i];
                }
                str.append(new String(tempBytes, "ISO8859-1"));
            }
            inputStream.close();
            resourcePool.put(filePath, str);
        }
        outs.flush();
        outs.close();
    }
