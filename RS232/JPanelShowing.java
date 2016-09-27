/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RS232;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author afunx
 */
public class JPanelShowing extends javax.swing.JPanel{
    //*****public int[][] meanValue = new int[2*PolarCoordinateToRect.RANGE][2*PolarCoordinateToRect.RANGE];
    //*****public static final int SIDE = 504;//栅格地图边长
    //*****public static final int CELL = SIDE/(2*PolarCoordinateToRect.RANGE);//栅格地图单位小格的半径

    public JPanelShowing(){
        super();
        //setSize(550,550);
    }
    
    @Override
    protected void paintComponent(Graphics g){
        Graphics gTemp = g.create();
        g.setColor(Color.black);
        g.fillRect(0, 0, 501, 501);
        gTemp.dispose();
    }

    //*****
    //public void upDate(int i,int j){//i,j表示栅格数组的位置
    //    Graphics g = this.getGraphics();
    //    Graphics gTemp = g;
    //    gTemp.setColor(new Color(5*meanValue[i][j],5*meanValue[i][j],5*meanValue[i][j]));
    //    gTemp.clearRect(i*CELL,SIDE-(j+1)*CELL, CELL, CELL);//清除原背景色
    //    gTemp.fillRect(i*CELL,SIDE-(j+1)*CELL, CELL, CELL);
    //    gTemp.dispose();
    //}

}
