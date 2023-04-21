    boolean downloadFile(CommandIF comm) {
        boolean flag = false;
        List<String> recp = comm.getRecipientNames();
        for (String str : recp) {
            if (str.equals(sendername)) {
                flag = true;
            }
        }
        flag = flag && cap.acceptFile;
        String fileName = comm.getFileName();
        String url = "http://" + cap.server + "/chat/Upload/" + comm.getMsgFrom() + fileName;
        url = url.replaceAll(" ", "%20");
        try {
            if (flag) {
                URL url1 = new URL(url);
                URLConnection urlconn = url1.openConnection();
                int len = urlconn.getContentLength();
                ByteArrayOutputStream tempBuffer;
                if (len < 0) {
                    tempBuffer = new ByteArrayOutputStream();
                } else {
                    tempBuffer = new ByteArrayOutputStream(len);
                }
                int ch;
                InputStream instream = urlconn.getInputStream();
                while ((ch = instream.read()) >= 0) {
                    tempBuffer.write(ch);
                }
                byte[] b = tempBuffer.toByteArray();
                instream.close();
                tempBuffer.close();
                File out = new File(System.getProperty("user.home") + File.separator + fileName);
                FileOutputStream fos = new FileOutputStream(out);
                fos.write(b);
                fos.close();
                cap.acceptFile = false;
            }
        } catch (FileNotFoundException e) {
            flag = false;
            e.printStackTrace();
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
