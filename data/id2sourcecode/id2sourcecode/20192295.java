    public CompositeData updateBundlesFromURL(long[] bundleIdentifiers, String[] urls) throws IOException {
        try {
            List<Long> bundleIds = new ArrayList<Long>();
            List<Long> completedBundles = new ArrayList<Long>();
            boolean isSuccess = true;
            long errorBundleId = 0;
            String errorDetails = null;
            if (bundleIdentifiers != null && urls != null) {
                if (bundleIdentifiers.length != urls.length) {
                    throw new IllegalArgumentException("BundlesId array length is not equal to urls array length");
                }
                bundleIds.addAll(Arrays.asList(Utils.toLongArray(bundleIdentifiers)));
                for (int i = 0; bundleIds.size() > 0; i++) {
                    long bundleId = bundleIds.remove(0);
                    try {
                        Bundle bundle = visitor.getBundle(bundleId);
                        if (bundle == null) {
                            throw new IllegalArgumentException("Bundle ID is wrong: " + bundleId);
                        }
                        bundle.update(new URL(urls[i]).openStream());
                        completedBundles.add(bundleId);
                    } catch (Exception e) {
                        logVisitor.warning("Bundle update from URL error", e);
                        isSuccess = false;
                        errorBundleId = bundleId;
                        errorDetails = e.getMessage();
                        break;
                    }
                }
            }
            Map<String, Object> values = new HashMap<String, Object>();
            values.put(REMAINING, bundleIds.toArray(new Long[bundleIds.size()]));
            values.put(COMPLETED, completedBundles.toArray(new Long[completedBundles.size()]));
            values.put(BUNDLE_IN_ERROR, errorBundleId);
            values.put(ERROR, errorDetails);
            values.put(SUCCESS, isSuccess);
            return new CompositeDataSupport(BATCH_ACTION_RESULT_TYPE, values);
        } catch (Exception e) {
            logVisitor.warning("updateBundlesFromURL error", e);
            throw new IOException(e.getMessage());
        }
    }
