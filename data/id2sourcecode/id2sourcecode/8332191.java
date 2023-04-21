    public void run() {
        try {
            byte[] buff = new byte[1024];
            FileOutputStream fos = new FileOutputStream(this.fileName);
            int readCount = data.read(buff);
            while (readCount >= 0) {
                fos.write(buff, 0, readCount);
                readCount = data.read(buff);
            }
        } catch (Exception e) {
            log.error(this, e);
        }
        if (this.props != null) {
            for (String name : this.props.keySet()) {
                ScaleProperty prop = (ScaleProperty) this.props.get(name);
                if (prop == null) continue;
                String fn = whf.file.entity.File.convertIconFileName(this.fileName, name);
                File file = new File(fn);
                try {
                    file.createNewFile();
                    ScaleImage scaleImage = new ScaleImage(this.fileName, fn);
                    scaleImage.setProperties(prop);
                    scaleImage.run();
                } catch (Exception e) {
                    log.error(this, e);
                }
            }
        }
    }
