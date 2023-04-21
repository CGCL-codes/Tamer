    public void getPool() {
        REST myRest = null;
        try {
            myRest = new REST();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        groupId = properties.getProperty("groupID");
        pi = new PoolsInterface(properties.getProperty("apiKey"), properties.getProperty("secret"), myRest);
        myPhotoInterface = new PhotosInterface(properties.getProperty("apiKey"), properties.getProperty("secret"), myRest);
        myTagsInterface = new TagsInterface(properties.getProperty("apiKey"), properties.getProperty("secret"), myRest);
        myPeopleInterface = new PeopleInterface(properties.getProperty("apiKey"), properties.getProperty("secret"), myRest);
        myGeoInterface = new GeoInterface(properties.getProperty("apiKey"), properties.getProperty("secret"), myRest);
        full_pl = new PhotoList();
        if (writeIDs) {
            try {
                outIDs = new BufferedWriter(new FileWriter(properties.getProperty("photoIDs")));
            } catch (IOException e) {
                System.out.print("Exception ");
                e.printStackTrace();
            }
        }
        if (writeTags) {
            try {
                outTags = new BufferedWriter(new FileWriter(properties.getProperty("photoTags")));
            } catch (IOException e) {
                System.out.print("Exception ");
                e.printStackTrace();
            }
        }
        if (writeTitles) {
            try {
                outTitles = new BufferedWriter(new FileWriter(properties.getProperty("photoTitles")));
            } catch (IOException e) {
                System.out.print("Exception ");
                e.printStackTrace();
            }
        }
        if (writePeople) {
            try {
                outPeople = new BufferedWriter(new FileWriter(properties.getProperty("photoPeople")));
            } catch (IOException e) {
                System.out.print("Exception ");
                e.printStackTrace();
            }
        }
        if (writeLocation) {
            try {
                outLocation = new BufferedWriter(new FileWriter(properties.getProperty("photoLocation")));
                outLocation.write("ID\tLat\tLon");
            } catch (IOException e) {
                System.out.print("Exception ");
                e.printStackTrace();
            }
        }
        try {
            full_pl = pi.getPhotos(groupId, tagsFilter, perPage, page);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FlickrException e) {
            e.printStackTrace();
        }
        origNumPages = numPages = full_pl.getPages();
        numTotal = full_pl.getTotal();
        numExpected = numTotal - ((page - 1) * perPage);
        System.out.println("total num results in pool = " + Integer.toString(numTotal));
        System.out.println("Grabbing pages " + page + "-" + Integer.toString(numPages));
        System.out.println("Expected num results = " + Integer.toString(numExpected));
        for (int p = page; p <= numPages; p++) {
            System.out.print("getting page " + Integer.toString(p) + " list ");
            PhotoList page_pl = new PhotoList();
            try {
                page_pl = pi.getPhotos(groupId, tagsFilter, perPage, p);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
                System.out.println("!!! ERROR retrieving list of photos for page " + Integer.toString(p));
                if (perPage == 1) {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!! ERROR retrieving page for photo " + Integer.toString(p) + " so it is being skipped !!!!!!!!!!!!!!!");
                    faultCounter--;
                    numLost++;
                    continue;
                } else {
                    origPage = p;
                    page = (p - 1) * perPage + 1;
                    p = page;
                    faultCounter = perPage;
                    perPage = 1;
                    System.out.println("Going back to page " + Integer.toString(p) + " and grabbing 1 per page");
                }
                try {
                    page_pl = pi.getPhotos(groupId, tagsFilter, perPage, p);
                } catch (IOException eB) {
                    eB.printStackTrace();
                } catch (SAXException eB) {
                    eB.printStackTrace();
                    System.out.println("!!!!!!!!!!!!!!!!!!!!! ERROR retrieving page for photo " + Integer.toString(p) + " so it is being skipped !!!!!!!!!!!!!!!");
                    faultCounter--;
                    numLost++;
                    continue;
                } catch (FlickrException eB) {
                    eB.printStackTrace();
                }
                numPages = page_pl.getPages();
            } catch (FlickrException e) {
                e.printStackTrace();
            }
            System.out.print("(" + Integer.toString(page_pl.size()) + " photos)");
            for (int i = 0; i < page_pl.size(); i++) {
                myPhoto = (Photo) page_pl.get(i);
                photoId = myPhoto.getId();
                System.out.print(".");
                if (writeIDs) {
                    try {
                        outIDs.write(photoId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writeTitles) {
                    try {
                        outTitles.write(photoId + "\t" + myPhoto.getDateAdded().getTime() + "\t" + myPhoto.getTitle());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writePeople) {
                    try {
                        photoUserID = myPhoto.getOwner();
                        String uID = photoUserID.getId();
                        try {
                            photoUser = myPeopleInterface.getInfo(uID);
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (FlickrException e) {
                            e.printStackTrace();
                        }
                        outPeople.write(photoId + "\t" + photoUser.getUsername() + "\t" + photoUser.getRealName() + "\t" + photoUser.getLocation() + "\t" + Integer.toString(photoUser.getPhotosCount()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writeTags) {
                    myPhotoTags = (Photo) page_pl.get(i);
                    writeOutTags(photoId);
                }
                if (writeLocation) {
                    try {
                        photoLocation = myGeoInterface.getLocation(photoId);
                        try {
                            outLocation.write(photoId + "\t" + photoLocation.getLatitude() + "\t" + photoLocation.getLongitude());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (SAXException e1) {
                        e1.printStackTrace();
                    } catch (FlickrException e1) {
                        try {
                            outLocation.write(photoId + "\t" + null + "\t" + null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (writeIDs) {
                    try {
                        outIDs.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writeTitles) {
                    try {
                        outTitles.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writePeople) {
                    try {
                        outPeople.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writeLocation) {
                    try {
                        outLocation.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (writePhotos) {
                    try {
                        is = myPhotoInterface.getImageAsStream(myPhoto, com.aetrion.flickr.photos.Size.MEDIUM);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (FlickrException e) {
                        e.printStackTrace();
                    }
                    File f = new File(properties.getProperty("photoDir") + "/" + photoId + ".jpg");
                    try {
                        OutputStream out = new FileOutputStream(f);
                        byte buf[] = new byte[1024];
                        int len;
                        while ((len = is.read(buf)) > 0) out.write(buf, 0, len);
                        out.close();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                photoCounter++;
            }
            if (writeIDs) {
                try {
                    outIDs.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writeTitles) {
                try {
                    outTitles.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writeTags) {
                try {
                    outTags.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writePeople) {
                try {
                    outPeople.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writeLocation) {
                try {
                    outLocation.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (faultCounter > 1) {
                System.out.println("!faultCounter: " + Integer.toString(faultCounter));
                faultCounter--;
            } else if (faultCounter == 1) {
                System.out.println("!!!faultCounter: " + Integer.toString(faultCounter));
                faultCounter--;
                perPage = origPerPage;
                p = origPage;
                numPages = origNumPages;
            }
            System.out.println("");
        }
        if (writeIDs) {
            try {
                outIDs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writeTags) {
            try {
                outTags.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writeTitles) {
            try {
                outTitles.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writePeople) {
            try {
                outTitles.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writeLocation) {
            try {
                outLocation.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Grabbed " + photoCounter + " photos");
        System.out.println("Skipped " + numLost + " photos");
        System.out.println("Missing " + Integer.toString(numExpected - photoCounter - numLost) + " photos from an expected " + numExpected);
        System.out.println("!!!!!!!!!! DONE !!!!!!!!!!");
    }
