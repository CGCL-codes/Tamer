        public synchronized void update(Observable o, Object arg) {
            if (image == null || factories.length == 0) return;
            if (writeThread != null) {
                writeThread.stop = true;
                writeThread.interrupt();
                try {
                    writeThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (continuus && imageComponentComponent != null && factories.length == imageComponentComponent.sinks.length && image == imageComponentComponent.source.getImage()) {
                imageComponentComponent = new ImageComponentComponent(imageComponentComponent);
            } else {
                imageComponentComponent = new ImageComponentComponent();
            }
            setPaused(paused);
            writeThread = new WriteThread(imageComponentComponent.source);
            writeThread.start();
            revalidate();
        }
