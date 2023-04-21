    @Override
    public void checkData() {
        try {
            URLConnection conn = url.openConnection();
            if (editTime < conn.getLastModified()) {
                editTime = conn.getLastModified();
                currentSize = conn.getContentLength();
                System.out.println(currentSize);
                notifyListeners(this);
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
