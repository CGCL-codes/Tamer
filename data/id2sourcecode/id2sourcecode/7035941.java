    protected void writeFile() {
        if (doc != null && file != null) {
            StringBuffer buffer = new StringBuffer("");
            try {
                for (Node node = doc.getFirstChild(); node != null; node = node.getNextSibling()) {
                    writeNode(node, buffer, type != SVG_FILE || true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer.toString().getBytes("UTF-8"));
                FileOutputStream out = new FileOutputStream(file);
                FileChannel channel = out.getChannel();
                channel.write(byteBuffer);
                channel.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
