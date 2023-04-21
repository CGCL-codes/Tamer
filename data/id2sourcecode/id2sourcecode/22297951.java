        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            File basePath = new File("/opt/cvs-co/informa/test/data");
            FileUtils.copyFile(new File(basePath, "xmlhack-0.91-added-item.xml"), new File("/tmp/dummy.xml"));
        }
