    @Override
    public void run() {
        String localName = getRealName(fileName);
        String format = getFormat(localName);
        for (String _format : acceptFormats) {
            if (_format.equalsIgnoreCase(format)) {
                ImageReader ir = null;
                try {
                    Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(format);
                    ir = readers.next();
                    ir.setInput(ImageIO.createImageInputStream(new URL(fileName).openStream()));
                    if (this.checkImg(ir)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(filePath).append(localName);
                        File outTemp = new File(sb.toString());
                        if (!outTemp.exists()) {
                            ImageIO.write(ir.read(0), format, outTemp);
                            view.log("成功下载: " + sb.toString());
                        }
                    }
                } catch (IllegalArgumentException iie) {
                    view.log("由于没有找到图片解码器，无法下载: " + fileName);
                } catch (IOException e) {
                    view.log("由于网络或错误的图像地址，无法下载: " + fileName);
                } finally {
                    if (ir != null) {
                        ImageInputStream input = (ImageInputStream) (ir.getInput());
                        if (input != null) {
                            try {
                                input.close();
                            } catch (IOException e) {
                                view.log("无法关闭文件读取流。");
                            }
                        }
                        ir.abort();
                        ir.dispose();
                    }
                }
                return;
            }
        }
    }
