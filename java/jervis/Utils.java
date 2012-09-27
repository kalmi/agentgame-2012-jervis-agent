package kalmi;

import java.awt.Point;

public class Utils {
    public static boolean isVisible(Point from, MyDir mydir, Point target) {
        int dx = target.x - from.x;
        int dy = target.y - from.y;   
        int viewDistance = 10;

        // Return true, if the target is in the triangular view field 
        switch (mydir) {
            case up:
                if ((dy<=0)&&(dy*-1<=viewDistance)&&(Math.abs(dx))<=dy*-1) return true;
                return false;
            case right:
                if ((dx>=0)&&(dx   <=viewDistance)&&(Math.abs(dy))<=dx) return true;
                return false;
            case down:
                if ((dy>=0)&&(dy   <=viewDistance)&&(Math.abs(dx))<=dy) return true;
                return false;
            case left:
                if ((dx<=0)&&(dx*-1<=viewDistance)&&(Math.abs(dy))<=dx*-1) return true;
                return false;
        }
        
        // Otherwise return false
        return false;
    }
}
