            public void run() {
                LineIterator lit;
                try {
                    lit = con.newGroups(date, null);
                    System.out.println("NEW NEWS: ");
                    while (lit.hasNext()) {
                        System.out.println(lit.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
