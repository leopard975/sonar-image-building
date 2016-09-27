package RS232;
import java.awt.geom.Point2D.Double;
/**
 *  Point2D.Double的继承，附加一个数值单元，
 *  表示该点的强弱。
 * @author afunx
 */
public class PointPotence extends java.awt.geom.Point2D.Double{
    int potence;


    public PointPotence(){
        potence = 0;
    }

    @Override
    public String toString(){
        return "Point2D.Double["+x+", "+y+", "+ potence +"]";
    }
}
