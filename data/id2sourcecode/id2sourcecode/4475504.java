    public Interest handleContent(ContentObject result, Interest interest) {
        Log.finest("Interests registered: " + _interests.size() + " content object returned");
        ContentName interestName = null;
        if (_processedObjects.contains(result)) {
            Log.fine("FLOSSER: Got repeated content for interest: {0} content: {1}", interest, result.name());
        } else {
            Log.finest("FLOSSER: Got new content for interest {0} content name: {1}", interest, result.name());
            processContent(result);
            synchronized (_interests) {
                for (Entry<ContentName, Interest> entry : _interests.entrySet()) {
                    if (entry.getValue().equals(interest)) {
                        interestName = entry.getKey();
                        _interests.remove(interestName);
                        break;
                    }
                }
            }
            int prefixCount = interest.name().count();
            if (prefixCount == result.name().count()) {
                if (null == interest.exclude()) {
                    ArrayList<Exclude.Element> excludes = new ArrayList<Exclude.Element>();
                    excludes.add(new ExcludeComponent(result.digest()));
                    interest.exclude(new Exclude(excludes));
                    Log.finest("Creating new exclude filter for interest {0}", interest.name());
                } else {
                    if (interest.exclude().match(result.digest())) {
                        Log.fine("We should have already excluded content digest: " + DataUtils.printBytes(result.digest()));
                    } else {
                        Log.finest("Adding child component to exclude.");
                        interest.exclude().add(new byte[][] { result.digest() });
                    }
                }
                Log.finer("Excluding content digest: " + DataUtils.printBytes(result.digest()) + " onto interest {0} total excluded: " + interest.exclude().size(), interest.name());
            } else {
                if (null == interest.exclude()) {
                    ArrayList<Exclude.Element> excludes = new ArrayList<Exclude.Element>();
                    excludes.add(new ExcludeComponent(result.name().component(prefixCount)));
                    interest.exclude(new Exclude(excludes));
                    Log.finest("Creating new exclude filter for interest {0}", interest.name());
                } else {
                    if (interest.exclude().match(result.name().component(prefixCount))) {
                        Log.fine("We should have already excluded child component: {0}", ContentName.componentPrintURI(result.name().component(prefixCount)));
                    } else {
                        Log.finest("Adding child component to exclude.");
                        interest.exclude().add(new byte[][] { result.name().component(prefixCount) });
                    }
                }
                Log.finer("Excluding child " + ContentName.componentPrintURI(result.name().component(prefixCount)) + " total excluded: " + interest.exclude().size());
                ContentName newNamespace = null;
                try {
                    if (interest.name().count() == result.name().count()) {
                        newNamespace = new ContentName(interest.name(), result.digest());
                        Log.info("Not adding content exclusion namespace: {0}", newNamespace);
                    } else {
                        newNamespace = new ContentName(interest.name(), result.name().component(interest.name().count()));
                        Log.info("Adding new namespace: {0}", newNamespace);
                        handleNamespace(newNamespace, interest.name());
                    }
                } catch (IOException ioex) {
                    Log.warning("IOException picking up namespace: {0}", newNamespace);
                }
            }
        }
        if (null != interest) synchronized (_interests) {
            _interests.put(interest.name(), interest);
        }
        return interest;
    }
