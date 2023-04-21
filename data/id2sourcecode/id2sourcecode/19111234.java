    public boolean download(String address, String localFileName) {
        boolean result = false;
        int attemptsNumber = 0;
        int iRepeatCount = repeatCount;
        try {
            do {
                attemptsNumber++;
                OutputStream out = null;
                URLConnection conn = null;
                InputStream in = null;
                try {
                    URL url = new URL(address);
                    out = new BufferedOutputStream(new FileOutputStream(localFileName));
                    conn = url.openConnection();
                    in = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int numRead;
                    while ((numRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, numRead);
                    }
                    result = true;
                } catch (Exception ex) {
                    result = false;
                    Thread.sleep(repeatTimeout);
                    iRepeatCount--;
                    if (iRepeatCount > 0) System.out.println("Connection to file " + address + " ... Attempt number " + (attemptsNumber + 1));
                } finally {
                    if (in != null) in.close();
                    if (out != null) out.close();
                }
            } while (!result && iRepeatCount > 0);
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }
