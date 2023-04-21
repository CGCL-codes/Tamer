    public static byte[] loadFileBytes(String name) {
        try {
            URL url = Resourcer.class.getClassLoader().getResource(name);
            if (url != null) {
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[connection.getContentLength()];
                for (int read = 0; read < buffer.length; read += inputStream.read(buffer, read, buffer.length - read)) ;
                return buffer;
            } else {
                FileInputStream inputStream = new FileInputStream(name);
                byte[] buffer = new byte[(int) inputStream.getChannel().size()];
                for (int read = 0; read < buffer.length; read += inputStream.read(buffer, read, buffer.length - read)) ;
                return buffer;
            }
        } catch (IOException e) {
            return null;
        }
    }
