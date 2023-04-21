    private Support_TestWebData(String path, String type) {
        File file = new File(path);
        testLength = file.length();
        testLastModified = file.lastModified();
        testName = file.getName();
        testType = type;
        testDir = file.isDirectory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            while (in.available() > 0) {
                out.write(in.read());
            }
            in.close();
            out.flush();
            test0Data = out.toByteArray();
            out.close();
            test0DataAvailable = true;
            return;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
        }
    }
