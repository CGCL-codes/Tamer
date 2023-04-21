        @SuppressWarnings("static-access")
        public void run() {
            while (true) {
                if (play[this.id]) {
                    int[] pix = new int[25];
                    BufferedImage image1 = this.boty.createScreenCapture(new Rectangle(i + x[this.id], j + y[this.id], 5, 5));
                    PixelGrabber pg = new PixelGrabber(image1, 0, 0, 5, 5, pix, 0, 5);
                    try {
                        pg.grabPixels();
                    } catch (InterruptedException d) {
                        System.err.println("en attente des pixels");
                    }
                    if (pix[6] == -13884141 && pix[7] == -8954866) {
                        checkBankroll(this.id);
                        if (players[this.id].bankroll != -1) {
                            players[this.id].addAction(3, players[this.id].bankroll - bankroll[this.id], moment);
                            System.out.println("Seat " + this.id + " bets " + (players[this.id].bankroll - bankroll[this.id]));
                        } else {
                            System.out.println("Seat " + this.id + " bets" + " - Initialisation BR");
                        }
                        players[this.id].bankroll = bankroll[this.id];
                        while (pix[6] == -13884141 && pix[7] == -8954866) {
                            image1 = this.boty.createScreenCapture(new Rectangle(i + x[this.id], j + y[this.id], 5, 5));
                            pg = new PixelGrabber(image1, 0, 0, 5, 5, pix, 0, 5);
                            try {
                                pg.grabPixels();
                            } catch (InterruptedException d) {
                                System.err.println("en attente des pixels");
                            }
                        }
                    } else if (pix[6] == -15198445 && pix[7] == -7115253) {
                        checkBankroll(this.id);
                        if (players[this.id].bankroll != -1) {
                            players[this.id].addAction(0, 0, moment);
                            System.out.println("Seat " + this.id + " folds");
                        } else {
                            System.out.println("Seat " + this.id + " folds" + " - Initialisation BR");
                        }
                        players[this.id].bankroll = bankroll[this.id];
                        while (pix[6] == -15198445 && pix[7] == -7115253) {
                            image1 = this.boty.createScreenCapture(new Rectangle(i + x[this.id], j + y[this.id], 5, 5));
                            pg = new PixelGrabber(image1, 0, 0, 5, 5, pix, 0, 5);
                            try {
                                pg.grabPixels();
                            } catch (InterruptedException d) {
                                System.err.println("en attente des pixels");
                            }
                        }
                    } else if (pix[6] == -5340917 && pix[7] == -13621229) {
                        checkBankroll(this.id);
                        if (players[this.id].bankroll != -1) {
                            players[this.id].addAction(2, players[this.id].bankroll - bankroll[this.id], moment);
                            System.out.println("Seat " + this.id + " calls " + (players[this.id].bankroll - bankroll[this.id]));
                        } else {
                            System.out.println("Seat " + this.id + " calls" + " - Initialisation BR");
                        }
                        players[this.id].bankroll = bankroll[this.id];
                        while (pix[6] == -5340917 && pix[7] == -13621229) {
                            image1 = this.boty.createScreenCapture(new Rectangle(i + x[this.id], j + y[this.id], 5, 5));
                            pg = new PixelGrabber(image1, 0, 0, 5, 5, pix, 0, 5);
                            try {
                                pg.grabPixels();
                            } catch (InterruptedException d) {
                                System.err.println("en attente des pixels");
                            }
                        }
                    } else if (pix[6] == -5275381 && pix[7] == -4814837) {
                        checkBankroll(this.id);
                        if (players[this.id].bankroll != -1) {
                            players[this.id].addAction(4, players[this.id].bankroll - bankroll[this.id], moment);
                            System.out.println("Seat " + this.id + " raises " + (players[this.id].bankroll - bankroll[this.id]));
                        } else {
                            System.out.println("Seat " + this.id + " raises " + " - Initialisation BR");
                        }
                        players[this.id].bankroll = bankroll[this.id];
                        while (pix[6] == -5275381 && pix[7] == -4814837) {
                            image1 = this.boty.createScreenCapture(new Rectangle(i + x[this.id], j + y[this.id], 5, 5));
                            pg = new PixelGrabber(image1, 0, 0, 5, 5, pix, 0, 5);
                            try {
                                pg.grabPixels();
                            } catch (InterruptedException d) {
                                System.err.println("en attente des pixels");
                            }
                        }
                    } else if (pix[6] == -13883885 && pix[7] == -8954866) {
                        checkBankroll(this.id);
                        if (players[this.id].bankroll != -1) {
                            players[this.id].addAction(1, 0, moment);
                            System.out.println("Seat " + this.id + " checks ");
                        } else {
                            System.out.println("Seat " + this.id + " checks" + " - Initialisation BR");
                        }
                        players[this.id].bankroll = bankroll[this.id];
                        while (pix[6] == -13883885 && pix[7] == -8954866) {
                            image1 = this.boty.createScreenCapture(new Rectangle(i + x[this.id], j + y[this.id], 5, 5));
                            pg = new PixelGrabber(image1, 0, 0, 5, 5, pix, 0, 5);
                            try {
                                pg.grabPixels();
                            } catch (InterruptedException d) {
                                System.err.println("en attente des pixels");
                            }
                        }
                    } else if (pix[22] == -11978223 && pix[23] == -14607085) {
                        checkBankroll(this.id);
                        if (players[this.id].bankroll != -1) {
                            players[this.id].addAction(12, players[this.id].bankroll - bankroll[this.id], moment);
                            System.out.println("Seat " + this.id + " posts big blind " + (players[this.id].bankroll - bankroll[this.id]));
                        } else {
                            System.out.println("Seat " + this.id + " posts big blind" + " - Initialisation BR");
                        }
                        players[this.id].bankroll = bankroll[this.id];
                        while (pix[22] == -11978223 && pix[23] == -14607085) {
                            image1 = this.boty.createScreenCapture(new Rectangle(i + x[this.id], j + y[this.id], 5, 5));
                            pg = new PixelGrabber(image1, 0, 0, 5, 5, pix, 0, 5);
                            try {
                                pg.grabPixels();
                            } catch (InterruptedException d) {
                                System.err.println("en attente des pixels");
                            }
                        }
                    } else if (pix[22] == -11846639 && pix[23] == -14607085) {
                        checkBankroll(this.id);
                        if (players[this.id].bankroll != -1) {
                            players[this.id].addAction(11, players[this.id].bankroll - bankroll[this.id], moment);
                            System.out.println("Seat " + this.id + " posts small blind " + (players[this.id].bankroll - bankroll[this.id]));
                        } else {
                            System.out.println("Seat " + this.id + " posts small blind" + " - Initialisation BR");
                        }
                        players[this.id].bankroll = bankroll[this.id];
                        while (pix[22] == -11846639 && pix[23] == -14607085) {
                            image1 = this.boty.createScreenCapture(new Rectangle(i + x[this.id], j + y[this.id], 5, 5));
                            pg = new PixelGrabber(image1, 0, 0, 5, 5, pix, 0, 5);
                            try {
                                pg.grabPixels();
                            } catch (InterruptedException d) {
                                System.err.println("en attente des pixels");
                            }
                        }
                    } else {
                    }
                } else {
                }
                try {
                    this.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
