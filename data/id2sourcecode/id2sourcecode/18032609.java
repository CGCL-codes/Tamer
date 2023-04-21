                public void run() {
                    Object m = null;
                    if (e.getMessage() instanceof String) {
                        m = (String) e.getMessage();
                    }
                    if (e.getMessage() instanceof TestObject) {
                        TestObject m_ = (TestObject) e.getMessage();
                        m = m_.getId() + ":" + m_.getName();
                        System.out.println(m_.getId());
                        System.out.println(m_.getName());
                    }
                    e.getChannel().write("receive:" + m);
                    counter++;
                }
