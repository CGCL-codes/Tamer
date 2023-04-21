    public BufferedImage createImage(InputStream is) throws IOException {
        String version = System.getProperty("java.version");
        if (!version.startsWith("1.4")) {
            ImageIO.setUseCache(false);
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(PNG_SIGNATURE.length);
        for (int i = 0; i < PNG_SIGNATURE.length; i++) {
            if (bis.read() != PNG_SIGNATURE[i]) {
                bis.reset();
                BufferedImage img = ImageIO.read(bis);
                if (img == null) {
                    throw new IOException("Image broken!");
                }
                return img;
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < PNG_SIGNATURE.length; i++) {
            baos.write(PNG_SIGNATURE[i]);
        }
        DataOutputStream dos = new DataOutputStream(baos);
        DataInputStream dis = new DataInputStream(bis);
        int bitDepth = -1;
        StringBuffer sb = new StringBuffer();
        while (true) {
            int len = dis.readInt();
            sb.setLength(4);
            for (int i = 0; i < 4; i++) {
                char c = (char) dis.read();
                sb.setCharAt(i, c);
            }
            String type = sb.toString();
            if (sb.toString().equals("IHDR")) {
                dos.writeInt(len);
                dos.write(type.getBytes());
                int w = dis.readInt();
                int h = dis.readInt();
                bitDepth = dis.read();
                int misc = dis.readInt();
                int crc = dis.readInt();
                dos.writeInt(w);
                dos.writeInt(h);
                dos.write(bitDepth);
                dos.writeInt(misc);
                dos.writeInt(crc);
            } else if (sb.toString().equals("PLTE")) {
                int padding = (2 << (bitDepth - 1)) * 3 - len;
                dos.writeInt(len + padding);
                dos.write(type.getBytes());
                for (int i = 0; i < len; i++) {
                    dos.write(dis.read());
                }
                for (int i = 0; i < padding; i++) {
                    dos.write(0);
                }
                dos.writeInt(dis.readInt());
            } else {
                dos.writeInt(len);
                dos.write(type.getBytes());
                for (int i = 0; i < len; i++) {
                    dos.write(dis.read());
                }
                dos.writeInt(dis.readInt());
                if (sb.toString().equals("IEND")) {
                    break;
                }
            }
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return ImageIO.read(bais);
    }
