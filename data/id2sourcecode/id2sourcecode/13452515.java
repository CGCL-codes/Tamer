        public void mouseMoved(final MouseEvent _e) {
            if (enCours()) {
                moved_ = true;
                effaceDessin();
                GrPoint pt = getPointReel(_e);
                if (ptAccro_ != null) pt = ptAccro_;
                grandRayon_ = Math.abs(pt.x_ - origine_.x_);
                petitRayon_ = Math.abs(pt.y_ - origine_.y_);
                if (keyPresse_.contains(KeyEvent.VK_SHIFT) || keyModifiers_.contains(KeyEvent.VK_SHIFT)) {
                    grandRayon_ = (grandRayon_ + petitRayon_) / 2;
                    petitRayon_ = grandRayon_;
                }
                dessineEllipse();
                if (support_ != null) support_.pointMove(origine_.x_ + grandRayon_, origine_.y_ + petitRayon_);
            }
        }
