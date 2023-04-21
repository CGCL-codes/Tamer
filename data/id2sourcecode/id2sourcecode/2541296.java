    public void setValue(Object value) throws IOException {
        if (value instanceof File) {
            ConfigurableApplicationContext app = (ConfigurableApplicationContext) Invoker.getApplicationContext();
            MvcResponse response = app.getMvcResponse();
            File f = (File) value;
            response.setInfo("Content-Disposition", "attachment;filename=" + f.getName() + ";");
            response.setLength((int) f.length());
            InputStream in = new FileInputStream(f);
            OutputStream out = response.processStream();
            try {
                byte[] buffer = new byte[3072];
                int length;
                while ((length = in.read(buffer)) != -1) out.write(buffer, 0, length);
            } finally {
                if (in != null) in.close();
            }
        }
    }
