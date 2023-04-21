    protected void run() throws Exception {
        if (null == reader) {
            throw new Exception("没有读取器");
        }
        if (null == writer) {
            throw new Exception("没有组装器");
        }
        while (reader.hasNext()) {
            if (writer.available()) {
                writer.write(reader.next());
            } else {
                reader.close();
                writer.close();
                break;
            }
        }
    }
