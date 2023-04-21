    public void setValue(Object value) throws IOException {
        if (value instanceof BrutosFile) {
            ConfigurableApplicationContext app = (ConfigurableApplicationContext) Invoker.getApplicationContext();
            MvcResponse response = app.getMvcResponse();
            BrutosFile f = (BrutosFile) value;
            if (f.getFile() != null) {
                response.setInfo("Content-Disposition", "attachment;filename=" + f.getFileName() + ";");
            }
            response.setLength((int) f.getFile().length());
            InputStream in = new FileInputStream(f.getFile());
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
