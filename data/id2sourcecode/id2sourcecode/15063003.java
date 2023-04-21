            public void onSuccess(String b) {
                VerticalPanel a = (VerticalPanel) map.get("Parent");
                HTML msg = new HTML();
                if (b.equals("ok")) msg = HTMLwriter.getSystemMessage("green", "Network created successfully." + " You can now view it."); else {
                    msg = HTMLwriter.getSystemMessage("red", "There was an error creating the Network. The name already exists.");
                }
                a.add(msg);
            }
