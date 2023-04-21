        public void run() {
            updateCurStatus("Checking HTTP Links", "");
            for (TreeNode node : this.urlList) {
                try {
                    URL url = new URL(node.getURL());
                    URLConnection connection = url.openConnection();
                    if (connection instanceof HttpURLConnection) {
                        httpValidate((HttpURLConnection) connection, node);
                    }
                } catch (IOException e) {
                    System.out.println("HTTP LINK Failed: " + node.getURL());
                    e.printStackTrace();
                }
            }
        }
