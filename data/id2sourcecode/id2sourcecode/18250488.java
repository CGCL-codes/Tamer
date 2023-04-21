    public boolean sizeOk() {
        int size;
        try {
            InputStream in = url.openStream();
            size = in.available();
            if (size > fileMaxSize) {
                System.out.println("Filtering " + url.toString() + " : over maximum file size");
                return false;
            } else {
                SiteCapturer.accumulatedSize += size;
                if (SiteCapturer.accumulatedSize < SiteCapturer.maxSize) {
                    return true;
                } else {
                    System.out.println("Reaching site size maximum, stopping the download");
                    return false;
                }
            }
        } catch (FileNotFoundException fn) {
            System.out.println("File not found : " + this.url);
        } catch (IOException e) {
            System.out.println("Connection failed to : " + this.url);
        } catch (NullPointerException npe) {
        }
        return true;
    }
