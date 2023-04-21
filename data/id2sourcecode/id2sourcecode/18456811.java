        protected void createRow(BView parent, int index) {
            BView icon = new BView(parent, 9, 2, 32, 32);
            NameValue value = (NameValue) mMenuList.get(index);
            try {
                URL url = new URL(value.getValue() + "icon.png");
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int success = inputStream.read(data);
                while (success != -1) {
                    baos.write(data, 0, success);
                    success = inputStream.read(data);
                }
                baos.close();
                inputStream.close();
                icon.setResource(createImage(baos.toByteArray()));
            } catch (Exception ex) {
                icon.setResource(mFolderIcon);
            }
            BText name = new BText(parent, 50, 4, parent.getWidth() - 40, parent.getHeight() - 4);
            name.setShadow(true);
            name.setFlags(RSRC_HALIGN_LEFT);
            name.setValue(Tools.trim(value.getName(), 40));
        }
