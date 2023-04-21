    String getPolicy() {
        StringBuffer content = new StringBuffer();
        try {
            FileInputStream fin = new FileInputStream("/system/certs/policysigned1.txt");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ch;
            while ((ch = fin.read()) != -1) {
                baos.write(ch);
            }
            fin.close();
            content.append(new String(baos.toByteArray()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("---EARL---", "length: " + content.length());
        return content.toString();
    }
