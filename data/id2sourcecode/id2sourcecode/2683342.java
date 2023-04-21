                public void execute() throws Exception {
                    URL url = new URL(urlString.toString());
                    String content = FileUtilities.getContents(url.openStream(), Integer.MAX_VALUE).toString();
                    assertNotNull(content);
                }
