    public void reverseRGB(final byte[] image) {
        byte swap;
        for (int i = 0; i < image.length; i += RGB_CHANNELS) {
            swap = image[i];
            image[i] = image[i + 2];
            image[i + 2] = swap;
        }
    }
