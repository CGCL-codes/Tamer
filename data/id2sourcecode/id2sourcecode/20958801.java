    public static void saveSystemLibrary(Class baseClass) {
        try {
            URL url = baseClass.getResource("system.dll");
            File file = new File("system.dll");
            if (!file.exists()) {
                byte[] buffer = new byte[1024];
                URLConnection con = url.openConnection();
                InputStream in = con.getInputStream();
                FileOutputStream out = new FileOutputStream(file);
                int n;
                while ((n = in.read(buffer, 0, buffer.length)) != -1) out.write(buffer, 0, n);
                in.close();
                out.close();
            }
            System.load(file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
