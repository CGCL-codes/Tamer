    private LayerInfo load(String request) {
        LayerInfo layerInfo = null;
        if (Navigator.isVerbose()) {
            System.out.println(request);
        }
        InputStream urlIn = null;
        try {
            URL url = new URL(request);
            URLConnection urlc = url.openConnection();
            urlc.setReadTimeout(Navigator.TIME_OUT);
            if (web3DService.getEncoding() != null) {
                urlc.setRequestProperty("Authorization", "Basic " + web3DService.getEncoding());
            }
            urlIn = urlc.getInputStream();
            if (urlIn != null) {
                layerInfo = load(urlIn);
            }
        } catch (org.gdi3d.xnavi.services.w3ds.x040.WrongLayerInfoVersionException wv) {
            if (Navigator.isVerbose()) {
                System.out.println("LayerInfo version is not 0.4.0");
            }
        } catch (java.net.SocketTimeoutException toe) {
            Object[] objects = { Navigator.i18n.getString("SYMBOLOGY_TIMEOUT") };
            JOptionPane.showMessageDialog(null, objects, Navigator.i18n.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                urlIn.close();
            } catch (Exception e) {
            }
        }
        return layerInfo;
    }
