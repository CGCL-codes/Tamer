    public static StringBuilder getFile(String target, String name) {
        InputStream is = null;
        OutputStream os = null;
        BufferedWriter bw;
        DataInputStream dis;
        StringBuilder sb = new StringBuilder();
        String line;
        File f = new File("hrtf");
        if (!f.isDirectory()) f.mkdir();
        try {
            URL url = new URL(target);
            is = url.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            int i = 5;
            int index = 0;
            double sound_buffer[][] = new double[2][200];
            while ((line = dis.readLine()) != null) {
                if (i > 0) {
                    i--;
                    continue;
                }
                String ear_values[] = line.split("\\s");
                String left_ear[] = ear_values[0].split("e");
                String right_ear[] = ear_values[1].split("e");
                int left_pow = 0;
                int right_pow = 0;
                if (left_ear[1].charAt(0) == '-') left_pow = -Integer.parseInt(left_ear[1].substring(1)); else left_pow = Integer.parseInt(left_ear[1].substring(1));
                if (right_ear[1].charAt(0) == '-') right_pow = -Integer.parseInt(right_ear[1].substring(1)); else right_pow = Integer.parseInt(right_ear[1].substring(1));
                sound_buffer[0][index] = Double.valueOf(left_ear[0]) * Math.pow(10, left_pow);
                sound_buffer[1][index] = Double.valueOf(right_ear[0]) * Math.pow(10, right_pow);
                index++;
            }
            Signal sound = new Signal(sound_buffer, 44100);
            sound.writeWav("hrtf/" + name.replace(".txt", ".wav"));
            System.out.println("Written Sound File to hrtf/" + name.replace(".txt", ".wav"));
        } catch (Exception e) {
            System.out.println("D: " + e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
        return sb;
    }
