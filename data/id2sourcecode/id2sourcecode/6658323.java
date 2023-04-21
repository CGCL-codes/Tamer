        public void run() {
            boolean ok = true;
            try {
                getContext().getLogger().message(this, "Started saving: " + url + " to " + cacheFile.getName());
                URLConnection conn = url.openConnection();
                InputStream is = conn.getInputStream();
                String contentType = conn.getContentType();
                if (contentType != null) getContext().getCacheManager().setContentType(url.toString(), contentType);
                FileOutputStream fos = new FileOutputStream(cacheFile);
                copyStreams(is, fos, 0);
            } catch (IOException ioe) {
                getContext().getLogger().error(this, ioe);
                getContext().getCacheManager().removeFromCache(url.toString());
                ok = false;
            }
            if (owner != null) owner.remove(this);
            getContext().getLogger().message(this, "Saving: " + url + (ok ? " complete!" : " aborted."));
        }
