    public void updatePositionsTransitionTypes(PlanFrame frame) {
        double desired_distance_min = 80;
        double desired_distance = 100;
        double desired_distance_max = 120;
        double toofar_distance = 500;
        double weight_repulsion = 0.5f;
        double weight_adyacency = 5.0f;
        double weight_toofar = 0.005f;
        double weight_hierarchy = 1.0f;
        double autostretch_amount = 1000;
        double autostretch_weight = 100.0f;
        double distance, dx, dy;
        double offset_x = 0, offset_y = 0;
        boolean desired_offset = false;
        double desired_offset_x = 0, desired_offset_y = 0;
        HashMap<Object, Location> elements = null;
        List<LocationLink> links = null;
        if (frame == null) {
            elements = m_elements;
            links = m_links;
        } else {
            elements = frame.m_elements;
            links = frame.m_links;
        }
        if (updateP()) {
            HashMap<Location, List<Push>> pushes = new HashMap<Location, List<Push>>();
            for (Location l1 : elements.values()) pushes.put(l1, new LinkedList<Push>());
            for (Location l1 : elements.values()) {
                for (Location l2 : elements.values()) {
                    if (l1 != l2) {
                        offset_x = 0;
                        offset_y = 0;
                        distance = Math.sqrt((l1.m_x - l2.m_x) * (l1.m_x - l2.m_x) + (l1.m_y - l2.m_y) * (l1.m_y - l2.m_y));
                        if (distance < desired_distance_min) {
                            if (distance == 0) {
                                if (l1.m_x == l2.m_x && l1.m_y == l2.m_y) {
                                    pushes.get(l2).add(new Push("Repulsion(d==0, identical)", rand.nextInt(11) - 5, rand.nextInt(11) - 5, 1.0f));
                                } else {
                                    dx = (l2.m_x - l1.m_x);
                                    dy = (l2.m_y - l1.m_y);
                                    distance = Math.sqrt(dx * dx + dy * dy);
                                    dx /= distance;
                                    dy /= distance;
                                    offset_x = (desired_distance_min) * dx / 2;
                                    offset_y = (desired_distance_min) * dy / 2;
                                    offset_x += rand.nextDouble() * m_randomize;
                                    offset_y += rand.nextDouble() * m_randomize;
                                    pushes.get(l2).add(new Push("Repulsion(d==0)", offset_x, offset_y, weight_repulsion));
                                    pushes.get(l1).add(new Push("Repulsion(d==0)", -offset_x, -offset_y, weight_repulsion));
                                }
                            } else {
                                dx = (l2.m_x - l1.m_x);
                                dy = (l2.m_y - l1.m_y);
                                distance = Math.sqrt(dx * dx + dy * dy);
                                dx /= distance;
                                dy /= distance;
                                offset_x = (desired_distance_min - distance) * dx / 2;
                                offset_y = (desired_distance_min - distance) * dy / 2;
                                offset_x += rand.nextDouble() * m_randomize;
                                offset_y += rand.nextDouble() * m_randomize;
                                pushes.get(l2).add(new Push("Repulsion", offset_x, offset_y, weight_repulsion));
                                pushes.get(l1).add(new Push("Repulsion", -offset_x, -offset_y, weight_repulsion));
                            }
                        }
                    }
                }
            }
            for (LocationLink l : links) {
                Location l2 = m_all_elements.get(l.m_a);
                Location l1 = m_all_elements.get(l.m_b);
                if (l1 == null) System.err.println("No element for " + l.m_a);
                if (l2 == null) System.err.println("No element for " + l.m_b);
                if (l1 != null && l2 != null && l1 != l2 && !(l instanceof WeakLocationLink)) {
                    desired_offset = false;
                    if (l1.m_element instanceof PlanState && l2.m_element instanceof PreTransition && ((PlanState) l1.m_element).getPlan() == ((PreTransition) l2.m_element).getPlan()) {
                        desired_offset_x = 0;
                        desired_offset_y = -desired_distance;
                        desired_offset = true;
                    }
                    if (l1.m_element instanceof PreTransition && l2.m_element instanceof PlanState && ((PlanState) l2.m_element).getPlan() == ((PreTransition) l1.m_element).getPlan()) {
                        desired_offset_x = 0;
                        desired_offset_y = desired_distance;
                        desired_offset = true;
                    }
                    if (l1.m_element instanceof PlanState && l2.m_element instanceof SuccessTransition && ((PlanState) l1.m_element).getPlan() == ((SuccessTransition) l2.m_element).getPlan()) {
                        desired_offset_x = 0;
                        desired_offset_y = desired_distance;
                        desired_offset = true;
                    }
                    if (l1.m_element instanceof SuccessTransition && l2.m_element instanceof PlanState && ((PlanState) l2.m_element).getPlan() == ((SuccessTransition) l1.m_element).getPlan()) {
                        desired_offset_x = 0;
                        desired_offset_y = -desired_distance;
                        desired_offset = true;
                    }
                    if (l1.m_element instanceof PlanState && l2.m_element instanceof FailureTransition && ((PlanState) l1.m_element).getPlan() == ((FailureTransition) l2.m_element).getPlan()) {
                        desired_offset_x = -desired_distance;
                        desired_offset_y = 0;
                        desired_offset = true;
                    }
                    if (l1.m_element instanceof FailureTransition && l2.m_element instanceof PlanState && ((PlanState) l2.m_element).getPlan() == ((FailureTransition) l1.m_element).getPlan()) {
                        desired_offset_x = desired_distance;
                        desired_offset_y = 0;
                        desired_offset = true;
                    }
                    if (desired_offset) {
                        double mx = (l1.m_x + l2.m_x) / 2;
                        double my = (l1.m_y + l2.m_y) / 2;
                        double dx1 = mx - desired_offset_x / 2;
                        double dy1 = my - desired_offset_y / 2;
                        double dx2 = mx + desired_offset_x / 2;
                        double dy2 = my + desired_offset_y / 2;
                        offset_x = dx1 - l1.m_x;
                        offset_y = dy1 - l1.m_y;
                        pushes.get(l1).add(new Push("Link (DO)", offset_x, offset_y, weight_adyacency));
                        offset_x = dx2 - l2.m_x;
                        offset_y = dy2 - l2.m_y;
                        pushes.get(l2).add(new Push("Link (DO)", offset_x, offset_y, weight_adyacency));
                    } else {
                        distance = Math.sqrt((l1.m_x - l2.m_x) * (l1.m_x - l2.m_x) + (l1.m_y - l2.m_y) * (l1.m_y - l2.m_y));
                        if (distance > desired_distance_max) {
                            dx = (l2.m_x - l1.m_x);
                            dy = (l2.m_y - l1.m_y);
                            dx /= distance;
                            dy /= distance;
                            offset_x = (desired_distance_max - distance) * dx / 2;
                            offset_y = (desired_distance_max - distance) * dy / 2;
                            offset_x += rand.nextDouble() * m_randomize;
                            offset_y += rand.nextDouble() * m_randomize;
                            pushes.get(l1).add(new Push("Link (~DO)", -offset_x, -offset_y, weight_adyacency));
                            pushes.get(l2).add(new Push("Link (~DO)", offset_x, offset_y, weight_adyacency));
                        }
                    }
                }
            }
            {
                double min_distance = 0, d;
                Location closest = null;
                for (Location l1 : elements.values()) {
                    closest = null;
                    for (Location l2 : elements.values()) {
                        if (l1 != l2) {
                            d = l1.distance(l2);
                            if (closest == null || d < min_distance) {
                                min_distance = d;
                                closest = l2;
                            }
                            if (min_distance < toofar_distance) break;
                        }
                    }
                    if (closest != null && min_distance > toofar_distance) {
                        dx = (closest.m_x - l1.m_x);
                        dy = (closest.m_y - l1.m_y);
                        dx /= min_distance;
                        dy /= min_distance;
                        offset_x = (toofar_distance - min_distance) * dx / 2;
                        offset_y = (toofar_distance - min_distance) * dy / 2;
                        offset_x += rand.nextDouble() * m_randomize;
                        offset_y += rand.nextDouble() * m_randomize;
                        pushes.get(closest).add(new Push("Too Far (closest)", offset_x, offset_y, weight_toofar));
                        pushes.get(l1).add(new Push("Too Far", -offset_x, -offset_y, weight_toofar));
                    }
                }
            }
            for (Location l : elements.values()) {
                if (l.m_element instanceof PlanFrame) {
                    updatePositionsTransitionTypes((PlanFrame) (l.m_element));
                    offset_x = ((PlanFrame) (l.m_element)).m_x - l.m_x;
                    offset_y = ((PlanFrame) (l.m_element)).m_y - l.m_y;
                    pushes.get(l).add(new Push("Hierarchy", offset_x, offset_y, weight_hierarchy));
                }
            }
            {
                Push largest = null;
                for (Location l : pushes.keySet()) {
                    double xinc = 0;
                    double yinc = 0;
                    double weight = 0;
                    for (Push p : pushes.get(l)) {
                        xinc += p.x * p.strength;
                        yinc += p.y * p.strength;
                        weight += p.strength;
                        if (largest == null || (p.x + p.y) > (largest.x + largest.y)) {
                            largest = p;
                        }
                    }
                    if (m_autostretch && l.m_element == m_head) {
                        yinc -= autostretch_amount * autostretch_weight;
                        weight += autostretch_weight;
                    }
                    if (weight > 0) {
                        xinc /= weight;
                        yinc /= weight;
                        l.m_x += xinc;
                        l.m_y += yinc;
                    }
                }
            }
        }
        {
            boolean first = true;
            int min_x = 0, max_x = 0;
            int min_y = 0, max_y = 0;
            double center_x, center_y;
            for (Location l : elements.values()) {
                Rectangle r = l.boundingBox();
                if (first) {
                    min_x = r.x;
                    min_y = r.y;
                    max_x = r.x + r.width;
                    max_y = r.y + r.height;
                    first = false;
                } else {
                    if (r.x < min_x) min_x = r.x;
                    if (r.x + r.width > max_x) max_x = r.x + r.width;
                    if (r.y < min_y) min_y = r.y;
                    if (r.y + r.height > max_y) max_y = r.y + r.height;
                }
            }
            min_x -= 50;
            max_x += 50;
            min_y -= 50;
            max_y += 50;
            center_x = (min_x + max_x) / 2;
            center_y = (min_y + max_y) / 2;
            if (frame != null) {
                int old_x = frame.m_x, old_y = frame.m_y;
                frame.m_x = (int) center_x;
                frame.m_y = (int) center_y;
                frame.move(old_x - frame.m_x, old_y - frame.m_y);
                frame.m_width = max_x - min_x;
                frame.m_height = max_y - min_y;
            } else {
                for (Location l : elements.values()) {
                    l.m_x -= (center_x - (getWidth() / 2));
                    l.m_y -= (center_y - (67 + ((getHeight() - 67) / 2)));
                    if (l.m_element instanceof PlanFrame) {
                        ((PlanFrame) l.m_element).move((int) -(center_x - (getWidth() / 2)), (int) -(center_y - (67 + ((getHeight() - 67) / 2))));
                    }
                }
                if (m_vp_autozoom && max_x - min_x != 0 && max_y - min_y != 0) {
                    double required_zoom_x = ((double) getWidth()) / (max_x - min_x);
                    double required_zoom_y = ((double) getHeight() - 67) / (max_y - min_y);
                    double required_zoom = Math.min(required_zoom_x, required_zoom_y);
                    m_vp_zoom = required_zoom;
                }
                if (m_vp_zoom > m_max_zoom) m_vp_zoom = m_max_zoom;
                if (m_vp_zoom < m_min_zoom) m_vp_zoom = m_min_zoom;
                if (m_vp_viewed_zoom != m_vp_zoom) m_vp_viewed_zoom = (3 * m_vp_viewed_zoom + m_vp_zoom) / 4;
                m_randomize *= 0.975f;
            }
        }
    }
