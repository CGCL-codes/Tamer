            public void actionPerformed(ActionEvent ev) {
                try {
                    File webXML = Application.current().getRelativeFile("WEB-INF/web.xml");
                    InputStream is = new FileInputStream(webXML);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[128];
                    int size;
                    while ((size = is.read(bytes)) != -1) baos.write(bytes, 0, size);
                    is.close();
                    TextArea ta = (TextArea) new TextArea(baos.toString()).setSize(600, 400);
                    MessageBox.confirm("", "", ta, "");
                } catch (Exception e) {
                    if (e instanceof RuntimeException) throw (RuntimeException) e;
                    throw new RuntimeException(e);
                }
            }
