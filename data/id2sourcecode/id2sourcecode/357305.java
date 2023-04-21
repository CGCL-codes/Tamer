    public static void download(String strUrl, String strFileDest) {
        try {
            URL url = new URL(strUrl);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(strFileDest));
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            while (((numRead = in.read(buffer)) != -1) && (!Thread.currentThread().isInterrupted())) {
                out.write(buffer, 0, numRead);
            }
            in.close();
            out.close();
        } catch (MalformedURLException e) {
            System.out.println("Internal Error. Malformed URL.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("Internal Error. Could not find file.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Internal I/O Error.");
            e.printStackTrace();
        }
    }
