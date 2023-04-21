    public CompositeData installBundlesFromURL(String[] locations, String[] urls) throws IOException {
        try {
            List<String> remainingLocations = new ArrayList<String>();
            List<Long> completedBundles = new ArrayList<Long>();
            boolean isSuccess = true;
            String errorBundleLocation = null;
            String errorDetails = null;
            if (locations != null && urls != null) {
                if (locations.length != urls.length) {
                    throw new IllegalArgumentException("Locations array length is not equal to urls array length");
                }
                remainingLocations.addAll(Arrays.asList(locations));
                for (int i = 0; remainingLocations.size() > 0; i++) {
                    String location = remainingLocations.remove(0);
                    try {
                        Bundle bundle = visitor.installBundle(location, new URL(urls[i]).openStream());
                        completedBundles.add(bundle.getBundleId());
                    } catch (Exception e) {
                        isSuccess = false;
                        errorBundleLocation = location;
                        errorDetails = e.getMessage();
                        break;
                    }
                }
            }
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(REMAINING, remainingLocations.toArray(new String[remainingLocations.size()]));
            values.put(COMPLETED, completedBundles.toArray(new Long[completedBundles.size()]));
            values.put(BUNDLE_IN_ERROR, errorBundleLocation);
            values.put(ERROR, errorDetails);
            values.put(SUCCESS, isSuccess);
            return new CompositeDataSupport(BATCH_INSTALL_RESULT_TYPE, values);
        } catch (Exception e) {
            logVisitor.warning("Batch installBundlesFromURL error", e);
            throw new IOException(e.getMessage());
        }
    }
