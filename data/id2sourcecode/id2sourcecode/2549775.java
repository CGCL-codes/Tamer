    public static void applyTexture(ShadedNull node, X3DTexture texture, X3DAppearance appearance, Phong phong) {
        if (texture instanceof X3DImageTexture) {
            X3DImageTexture imgTex = (X3DImageTexture) texture;
            ImageAdapter ia = null;
            boolean loadedFromWeb = false;
            File f = null;
            if (imgTex.getImg() == null) {
                URL url = null;
                Workbench wb = Workbench.current();
                int imageCount = imgTex.getUrl().length;
                for (int imageIndex = 0; imageIndex < imageCount; imageIndex++) {
                    try {
                        String imgpath = imgTex.getUrl()[imageIndex];
                        if (imgpath.toLowerCase().startsWith("http://")) {
                            String filename = imgpath.substring(imgpath.lastIndexOf("/") + 1, imgpath.lastIndexOf("."));
                            String fileext = imgpath.substring(imgpath.lastIndexOf("."), imgpath.length());
                            f = File.createTempFile(filename, fileext);
                            url = new URL(imgpath);
                            InputStream is = url.openStream();
                            FileOutputStream os = new FileOutputStream(f);
                            byte[] buffer = new byte[0xFFFF];
                            for (int len; (len = is.read(buffer)) != -1; ) os.write(buffer, 0, len);
                            is.close();
                            os.close();
                            url = f.toURI().toURL();
                            loadedFromWeb = true;
                        } else {
                            if (imgpath.startsWith("/") || (imgpath.charAt(1) == ':')) {
                            } else {
                                URL x3durl = X3DImport.getTheImport().getUrl();
                                imgpath = Util.getRealPath(x3durl) + imgpath;
                            }
                            f = new File(imgpath);
                            url = f.toURI().toURL();
                            Object testContent = url.getContent();
                            if (testContent == null) continue;
                            loadedFromWeb = false;
                        }
                        FileFactory ff = ImageReader.getFactory(wb.getRegistry());
                        ia = (FixedImageAdapter) ff.addFromURL(wb.getRegistry(), url, null, wb);
                        if (ia != null) break;
                    } catch (MalformedURLException e) {
                    } catch (IOException e) {
                    } finally {
                        if (loadedFromWeb && f != null) {
                            f.delete();
                        }
                    }
                }
                if (ia == null) return;
                imgTex.setImg(ia);
            } else {
                ia = imgTex.getImg();
            }
            ImageMap i = new ImageMap();
            i.setImageAdapter(ia);
            phong.setDiffuse(i);
            if (appearance.getTextureTransform() != null) {
                X3DTextureTransform texTrans = appearance.getTextureTransform();
                AffineUVTransformation uvTrans = new AffineUVTransformation();
                uvTrans.setAngle(-texTrans.getRotation());
                uvTrans.setOffsetU(-texTrans.getTranslation().x);
                uvTrans.setOffsetV(-texTrans.getTranslation().y);
                uvTrans.setScaleU(texTrans.getScale().x);
                uvTrans.setScaleV(texTrans.getScale().y);
                i.setInput(uvTrans);
            }
        }
    }
