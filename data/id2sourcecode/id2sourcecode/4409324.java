    public void run() {
        try {
            int n = -1;
            area.setText(null);
            url = new URL(text.getText().trim());
            InputStream in = url.openStream();
            while ((n = in.read(b)) != -1) {
                String s = new String(b, 0, n);
                area.append(s);
            }
        } catch (MalformedURLException e1) {
            text.setText("" + e1);
            return;
        } catch (IOException e1) {
            text.setText("" + e1);
            return;
        }
    }
