    public static void main(String args[]) {
        if (args.length < 3) {
            System.out.println(usage);
            System.exit(0);
        }
        long delay = Long.valueOf(args[1]).longValue();
        DataOutputStream bufferOut = null;
        DataOutputStream fileOut = null;
        try {
            ByteArrayOutputStream bout;
            bufferOut = new DataOutputStream(bout = new ByteArrayOutputStream());
            fileOut = new DataOutputStream(new FileOutputStream(new File(args[0])));
            int maxw = 0;
            int maxh = 0;
            for (int i = 2; i < args.length; i++) {
                File f = new File(args[i]);
                byte[] b = fileToByteArray(f);
                ImageIcon icon = new ImageIcon(b);
                int w = icon.getIconWidth();
                int h = icon.getIconHeight();
                if (w > maxw) maxw = w;
                if (h > maxh) maxh = h;
                bufferOut.writeLong(b.length);
                bufferOut.writeLong(delay);
                bufferOut.write(b, 0, b.length);
            }
            fileOut.writeInt(maxw);
            fileOut.writeInt(maxh);
            fileOut.writeLong((long) (delay * args.length));
            fileOut.write(bout.toByteArray());
            try {
                bout.close();
            } catch (IOException e) {
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not read/write data");
            System.exit(1);
        } finally {
            try {
                bufferOut.close();
            } catch (IOException e) {
            }
            try {
                fileOut.close();
            } catch (IOException e) {
            }
        }
        System.exit(0);
    }
