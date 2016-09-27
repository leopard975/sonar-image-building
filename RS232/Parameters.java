/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RS232;

/**
 *
 * @author ROV03
 */
public class Parameters {
    public static int RANGE = -1;//栅格地图有效数据位
    public static final int FRAME = 265;//一帧收到的数据字节数
    public static double angle;//接收到的角度
    public static boolean drawReady = false;//是否准备画栅格图
    public static int RESOLUTION =250;
    //控制命令
    public static byte[] commandBytes = {-2,68,16,1,0,0,67,0,6,
    1,20,60,120,3,6,0,0,0,0,25,8,6,0,0,5,100,-3};
    public static void main(String args[]){
        
    }
}
