    private void setPlant() {
        m_tvCommonName.setText(m_plant.getCommonName());
        m_tvScientificName.setText(m_plant.getScientificName());
        ImageView iv = (ImageView) findViewById(R.id.plantPhoto);
        if (!m_plant.getImageLocation().equals("")) {
            try {
                URL url = new URL(m_plant.getImageLocation());
                URLConnection connection = url.openConnection();
                connection.setUseCaches(true);
                InputStream is = (InputStream) connection.getContent();
                Drawable d = Drawable.createFromStream(is, m_plant.getImageLocation());
                iv.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
