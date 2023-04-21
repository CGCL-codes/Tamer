    public void process() {
        this.downlist = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.inputFile));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    this.downlist.add(line);
                }
            }
            br.close();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            int counts = 0;
            for (String f : this.downlist) {
                String filename = this.saveDir + "\\" + f.substring(f.lastIndexOf("/") + 1);
                URL url = new URL(f);
                bis = new BufferedInputStream(url.openStream());
                bos = new BufferedOutputStream(new FileOutputStream(filename));
                byte[] bytes = new byte[128];
                int len = 0;
                while ((len = bis.read(bytes)) > 0) {
                    bos.write(bytes, 0, len);
                }
                bis.close();
                bos.flush();
                bos.close();
                counts++;
                System.out.println("下载完成[" + counts + "]" + f);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
