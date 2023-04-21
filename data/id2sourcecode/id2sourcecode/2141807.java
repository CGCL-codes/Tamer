    public static void fileTest() {
        String path = "/home/test/abcd.txt";
        File file = new File(path);
        System.out.println("isfile:" + file.isFile());
        if (file.exists()) {
            if (file.isFile()) {
                System.out.println("status: file");
                try {
                    String str = FileUtils.readFileToString(file);
                    System.out.println("content: \n" + str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("status: dir");
                File[] children = file.listFiles();
                if (children != null) {
                    for (File f : children) {
                        if (f.isFile()) {
                            System.out.print("file:");
                        } else if (f.isDirectory()) {
                            System.out.print("dir:");
                        }
                        System.out.println(f.getAbsolutePath());
                    }
                } else {
                    System.out.println("no children~");
                }
            }
        } else {
            System.out.println("status: lost");
            System.out.println("delete on lost: " + file.delete());
        }
    }
