    private void updateLabelImages(JLabel l, int dpi) {
        File dir = new File(l.getText());
        if (dir.exists() && dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            for (File f : fileList) {
                try {
                    if (ImageIO.read(f) != null) {
                        com.codename1.ui.Image img = res.getImage(f.getName());
                        if (img == null) {
                            EditableResources.MultiImage newImage = new EditableResources.MultiImage();
                            newImage.setDpi(new int[] { dpi });
                            newImage.setInternalImages(new com.codename1.ui.EncodedImage[] { com.codename1.ui.EncodedImage.create(new FileInputStream(f)) });
                            res.setMultiImage(f.getName(), newImage);
                        } else {
                            Object o = res.getResourceObject(f.getName());
                            if (o instanceof EditableResources.MultiImage) {
                                EditableResources.MultiImage existing = (EditableResources.MultiImage) o;
                                EditableResources.MultiImage newImage = new EditableResources.MultiImage();
                                int[] dpis = new int[existing.getDpi().length + 1];
                                System.arraycopy(existing.getDpi(), 0, dpis, 0, existing.getDpi().length);
                                dpis[existing.getDpi().length] = dpi;
                                com.codename1.ui.EncodedImage[] images = new com.codename1.ui.EncodedImage[existing.getDpi().length + 1];
                                System.arraycopy(existing.getInternalImages(), 0, images, 0, existing.getDpi().length);
                                images[existing.getDpi().length] = com.codename1.ui.EncodedImage.create(new FileInputStream(f));
                                newImage.setDpi(dpis);
                                newImage.setInternalImages(images);
                                res.setMultiImage(f.getName(), newImage);
                            }
                        }
                    }
                } catch (IOException ex) {
                }
            }
        }
    }
