            public void actionPerformed(ActionEvent evt) {
                RandomContactWriter writer = new RandomContactWriter();
                writers.add(writer);
                writer.setList(source);
                writer.setPriority(Thread.NORM_PRIORITY);
                writer.start();
                startButton.setText("Start (" + writers.size() + ")");
            }
