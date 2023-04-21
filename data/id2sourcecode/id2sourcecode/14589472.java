        @Override
        protected Boolean doInBackground(Integer... params) {
            int index = params[0];
            for (int i = index; i < imgs.size(); i++) {
                String fileName = getFileName(imgs.get(i).get("url"));
                boolean isOk = false;
                try {
                    isOk = fileUtil.createImg(fileName, new URL(imgs.get(i).get("url")).openStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    imgs.get(i).put("isOk", isOk ? "y" : "n");
                }
                publishProgress(i);
            }
            return true;
        }
