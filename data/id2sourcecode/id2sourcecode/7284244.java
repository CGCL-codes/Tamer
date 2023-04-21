    private long download(String fileName) {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        long numWritten = 0;
        try {
            URL url = new URL(address + fileName);
            String localName = destination + File.separator + fileName;
            File f = new File(localName);
            if (!f.exists() || (f.exists() && !f.isDirectory() && (f.length() == 0))) {
                System.out.println(localName + "....");
                out = new BufferedOutputStream(new FileOutputStream(localName));
                conn = url.openConnection();
                in = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int numRead;
                while ((numRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, numRead);
                    numWritten += numRead;
                }
                System.out.println("\t" + numWritten);
            } else if (!f.isDirectory()) {
                System.out.println(localName + "\t already exist!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
        return numWritten;
    }
