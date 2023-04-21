    public void doit(String resourceName, boolean doPrint) {
        InputStream i;
        i = this.getClass().getResourceAsStream(resourceName);
        if (i == null) {
            System.out.println("Could not open " + resourceName);
        } else if (doPrint) {
            byte buf[] = new byte[100];
            int readsize;
            do {
                try {
                    readsize = i.read(buf, 0, buf.length);
                    if (readsize != -1) {
                        System.out.write(buf, 0, readsize);
                    }
                } catch (IOException e) {
                    System.out.println("Got IOException");
                    e.printStackTrace();
                    break;
                }
            } while (readsize != -1);
        } else {
            System.out.println("Successfully opened " + resourceName);
        }
    }
