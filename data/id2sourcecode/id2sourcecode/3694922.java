            public void run() {
                LineIterator ait;
                try {
                    ait = con.newNews(group.getName(), group.getLastUpdate(), null);
                    System.out.println("NEW NEWS: ");
                    while (ait.hasNext()) {
                        System.out.println(ait.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
