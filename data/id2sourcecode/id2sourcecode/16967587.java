    public static void main(String[] args) {
        try {
            urlt = "http://im.baidu.com/download/BaiduHi_4.2_Beta.exe";
            fileName = "D:\\" + urlt.split("//")[1].split("/")[urlt.split("//")[1].split("/").length - 1];
            System.out.println(fileName);
            URL url = new URL(urlt);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            System.out.println("file size:" + http.getContentLength());
            tn = 3;
            len = http.getContentLength() / tn;
            File f = new File(fileName);
            if (f.exists()) {
                f.delete();
            }
            osf = new RandomAccessFile(f, "rw");
            osf.seek(http.getContentLength() - 1);
            osf.write(0);
            System.out.println("temp 文件长度：" + f.length());
            Thread t;
            for (int j = 0; j < tn; j++) {
                if (j == tn - 1) {
                    bn = len + (http.getContentLength() % tn);
                } else {
                    bn = len;
                }
                System.out.println("t" + j + "线程下载长度：" + bn + "起始字节：" + len * j);
                t = new DT(j, urlt, fileName, len * j, bn);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
