    public void centerImage(int largeur, int hauteur) {
        int width = getSize().width;
        int height = getSize().height;
        if (w > width && (w > h)) {
            destw = width * 95 / 100;
            desth = (h * width * 95) / (w * 100);
        } else if (h > height && (h >= w)) {
            desth = height * 90 / 100;
            destw = w * height * 90 / (h * 100);
        } else {
            desth = h;
            destw = w;
        }
        x = (width - destw) / 2;
        y = (height - desth) / 2;
        xLeft = (width + destw) / 2;
        yBottom = (height + desth) / 2;
    }
