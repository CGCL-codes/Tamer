            @Override
            public void run() {
                while (true) {
                    if (loadQueue.isEmpty()) {
                        try {
                            synchronized (imageLoadThread) {
                                imageLoadThread.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("ImageLoader::imageLoadThread", "Loading image in special thread...");
                    ThreadBlock block = null;
                    synchronized (loadQueue) {
                        block = loadQueue.poll();
                    }
                    if (null != block) {
                        try {
                            Method callback = block.receiver.getClass().getMethod(block.callbackname, View.class, Bitmap.class);
                            Bitmap bitmap = BitmapFactory.decodeStream(new URL(block.urlstr).openStream());
                            FileOutputStream fos = ImageLoader.this.context.openFileOutput(block.fileName, Context.MODE_PRIVATE);
                            Log.i("ImageLoader::loadImageAsync", "Save image to " + block.fileName);
                            bitmap.compress(CompressFormat.PNG, 1, fos);
                            fos.close();
                            callback.invoke(block.receiver, block.view, bitmap);
                        } catch (Exception e) {
                            Log.e("ImageLoader::loadImageAsync", e.getMessage());
                        }
                    }
                }
            }
