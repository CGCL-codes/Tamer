        public void onRender(RenderTarget t) {
            Graphics g = t.getContext();
            int ox = getRect().getX();
            int oy = getRect().getY();
            for (int x = 0; x < mTarget.getSize().getX(); ++x) {
                for (int y = 0; y < mTarget.getSize().getY(); ++y) {
                    DataBattle.Tile tl = mTarget.getTile(x, y);
                    int tlx = ox + x * TILE_SIZE;
                    int tly = oy + y * TILE_SIZE;
                    if (tl.Filled) {
                        g.setColor(TILE_COLOR);
                        g.fillRect(tlx + 1, tly + 1, TILE_SIZE - 2, TILE_SIZE - 2);
                        if (tl.Upload) g.drawImage(UPLOAD_OVERLAY_IMAGE, tlx, tly, null);
                        if (tl.Credit > 0) {
                            int centerx = tlx + TILE_SIZE / 2;
                            int centery = tly + TILE_SIZE / 2;
                            g.setColor(CREDIT_COLOR);
                            g.drawRect(centerx - 1, centery - 1, 3, 3);
                            g.drawRect(centerx - 3, centery - 3, 7, 7);
                        }
                        if (tl.Agent != null) {
                            g.setColor(tl.Agent.getInfo().getColor());
                            g.fillRect(tlx + 1, tly + 1, TILE_SIZE - 2, TILE_SIZE - 2);
                            for (Vec v : Vec.getDirs()) {
                                DataBattle.Tile adjacenttl = mTarget.getTile(x + v.getX(), y + v.getY());
                                if (adjacenttl != null && adjacenttl.Agent == tl.Agent) {
                                    int atlx = ox + (x + v.getX()) * TILE_SIZE;
                                    int atly = oy + (y + v.getY()) * TILE_SIZE;
                                    int centerx = (atlx + tlx) / 2;
                                    int centery = (atly + tly) / 2;
                                    g.fillRect(centerx + TILE_SIZE / 2 - 2, centery + TILE_SIZE / 2 - 2, 4, 4);
                                }
                            }
                            if (tl.Agent.getPos().eq(x, y)) {
                                g.drawImage(tl.Agent.getInfo().getThumb(), tlx + 3, tly + 3, null);
                                switch(tl.Agent.getTurnState()) {
                                    case Done:
                                        g.drawImage(CHECK_IMAGE, tlx + 13, tly + 13, null);
                                }
                                int[] xs = { tlx + 1, tlx + 1, tlx + 1 + 5 };
                                int[] ys = { tly + TILE_SIZE - 2 - 5, tly + TILE_SIZE - 1, tly + TILE_SIZE - 1 };
                                g.setColor(tl.Agent.getTeam().getColor());
                                g.fillPolygon(xs, ys, 3);
                                g.setColor(TILE_COLOR);
                                g.drawLine(tlx + 1, tly + TILE_SIZE - 1 - 6, tlx + 1 + 5, tly + TILE_SIZE - 2);
                            }
                        }
                        Image overlayimg = null;
                        switch(tl.Overlay) {
                            case Neg:
                                overlayimg = RED_OVERLAY_IMAGE;
                                break;
                            case Pos:
                                overlayimg = BLUE_OVERLAY_IMAGE;
                                break;
                            case Mod:
                                overlayimg = GREEN_OVERLAY_IMAGE;
                                break;
                            case Sel:
                                overlayimg = WHITE_OVERLAY_IMAGE;
                                break;
                            case MoveTo:
                                overlayimg = MOVETO_OVERLAY_IMAGE;
                                break;
                            case Move:
                                overlayimg = MOVE_OVERLAY_IMAGE;
                                break;
                            case None:
                                break;
                            default:
                                break;
                        }
                        if (overlayimg != null) {
                            g.drawImage(overlayimg, tlx, tly, null);
                        }
                    } else {
                    }
                }
            }
            mParticleHandler.draw(getRect().getPos(), t);
            mFlashEffectHandler.draw(getRect().getPos(), t);
        }
