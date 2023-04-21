    public int converter(String urlPath, String savePath, String fileName) {
        int state = 0;
        try {
            url = new URL(urlPath);
            if (!exists(url)) {
                return 0;
            }
            urlConn = url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "utf-8"));
            folder = new File(savePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String filePath = savePath + "\\" + fileName;
            file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            out.write("<%@ page language=\"java\" pageEncoding=\"UTF-8\"%> \r\n");
            int c = 0;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            state = 1;
        } catch (Exception e) {
            state = -1;
            e.printStackTrace();
        } finally {
            this.close();
        }
        return state;
    }
