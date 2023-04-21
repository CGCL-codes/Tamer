    public static void main(String[] args) {
        FileDialog fd = new FileDialog(new Frame(), "Find a .cab file", FileDialog.LOAD);
        CabFile ca;
        CabEntry[] entries;
        OutputStream out = null;
        InputStream in;
        File out_file, out_dir;
        byte[] buffer = new byte[2048];
        int bytes_read;
        String input = "";
        boolean proceed = true;
        while (proceed) {
            fd.show();
            if (fd.getFile() != null) {
                try {
                    ca = new CabFile(new File(fd.getDirectory(), fd.getFile()));
                    entries = ca.getEntries();
                    out_dir = new File(fd.getDirectory(), fd.getFile() + " folder");
                    for (int i = 0; i < entries.length; i++) {
                        if (!out_dir.exists()) out_dir.mkdir();
                        System.out.println("Extracting " + entries[i].getName());
                        out_file = new File(out_dir, entries[i].getName());
                        out = new FileOutputStream(out_file);
                        in = ca.getInputStream(entries[i]);
                        while ((bytes_read = in.read(buffer)) != -1) out.write(buffer, 0, bytes_read);
                        in.close();
                        out.close();
                    }
                    System.out.println("done");
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        out.close();
                    } catch (IOException e2) {
                    }
                }
            } else {
                System.exit(0);
            }
            System.out.println("Do another (y/n)?");
            DataInputStream dis = new DataInputStream(System.in);
            try {
                input = dis.readLine();
            } catch (Exception blah) {
            }
            if (input.toLowerCase().startsWith("n")) proceed = false;
        }
    }
