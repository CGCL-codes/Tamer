    public static void main(String[] args) throws IOException {
        File[] files = new File("D:\\Servidores\\DATA\\ntcir\\PlaceMaker").listFiles();
        for (File f : files) {
            if (f.getName().endsWith(".xml")) {
                System.out.println("Zipping " + f.getName());
                ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(new File(f.getParentFile().getAbsolutePath() + "/" + f.getName().substring(0, f.getName().lastIndexOf(".")) + ".zip")));
                ZipEntry ze = new ZipEntry(f.getName());
                zout.putNextEntry(ze);
                FileInputStream fs = new FileInputStream(f);
                StreamsUtils.inputStream2OutputStream(fs, zout);
                fs.close();
                zout.flush();
                zout.close();
            }
        }
    }
