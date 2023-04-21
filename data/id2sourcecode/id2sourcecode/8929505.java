    public boolean writeFile(String url, File dist) {
        try {
            writeFile(new URL(url).openStream(), dist);
            return true;
        } catch (IOException e) {
            log.warn("�޷�����ͼƬ��", e);
            return false;
        }
    }
