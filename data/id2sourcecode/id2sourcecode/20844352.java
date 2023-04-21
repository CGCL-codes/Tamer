    private static void scaleHGT(String file, int width, int w, int h) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        int height = 0;
        while (true) {
            in.mark(1);
            if (in.read() < 0) {
                break;
            }
            in.reset();
            for (int x = 0; x < width; x++) {
                if (x % w == 0) {
                    System.out.write(in.read());
                    System.out.write(in.read());
                } else {
                    skip(in, 2);
                }
            }
            height++;
            skip(in, width * 2 * (h - 1));
        }
        in.close();
        System.out.flush();
        System.err.println("Output size is " + (width / w) + 'x' + height);
    }
