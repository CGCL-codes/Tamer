    private void resetLayout(String url) {
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            resetLayout(is);
            is.close();
            redraw();
        } catch (IOException E) {
            E.printStackTrace();
        } catch (ClassNotFoundException E) {
            E.printStackTrace();
        }
    }
