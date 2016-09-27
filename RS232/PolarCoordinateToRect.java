package RS232;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
/**
 *  极坐标转换到直角坐标的处理类
 * @author afunx
 */
public class PolarCoordinateToRect extends Thread{
    private static final int N = 250;//采样点的总数
    //public static final int RANGE = 125;//栅格地图的半径
    //public static final int FRAME = 265;//一帧收到的数据字节数
    public PointPotence[] pointPotences;
    public MapContent[][] mapContent;// = new MapContent[2*RANGE][2*RANGE];//栅格地图的内容
    public SonarData[] datas;
    public double radius;//量化半径
    //public static double angle;//接收到的角度
    public int[] receivedData; //= new int[FRAME];
    public int[] potences = new int[N+1];
    private static boolean first = true;//是否是第一次，是的话，记录下初始角度
    private static double firstAngle;//初始角，如果再次转到初始角，则数据单元清零。
   

    //public boolean drawReady;//是否准备画栅格图
    //private int count = 0;//每一圈之后，数据要清空

    //极坐标到直角坐标的转换
    public PolarCoordinateToRect(){
        //drawReady = false;
        radius = 1.0*Parameters.RANGE/N;
//        System.out.println("radius " + radius);
        pointPotences = new PointPotence[N+1];
        for(int index = 1; index<potences.length;index++){
            pointPotences[index] = new PointPotence();
        }
        ///*****
        datas = new SonarData[N];
        for(int i=0;i<N;i++){
                datas[i] = new SonarData();
        }
        ///*****
        mapContent = new MapContent[2*Parameters.RANGE][2*Parameters.RANGE];
        for(int i=0;i<2*Parameters.RANGE;i++){
            for(int j=0;j<2*Parameters.RANGE;j++){
                mapContent[i][j] = new MapContent();
            }
        }
    }

    //把从SerialThread收到的原始数据，进行初始化处理
    public void initiate(){
        //heading1 = 0.3*(((((byte6&0x3E)>>1)<<8)|(((byte6&0x01)<<7)|(byte5&0x7F)))-600);
        //count++;
        //计算角度值

       // Parameters.angle = 180+0.3*(((((receivedData[6]&0x3E)>>1)<<8)|(((receivedData[6]&0x01)<<7)|(receivedData[5]&0x7F)))-600);
        //System.out.println("角度变化："+ Parameters.angle);
        //如果是第一次的话，获得初始角度firstAngle
        if(first){
            firstAngle = Parameters.angle;
            first = false;
        }
        for(int i=1;i<N+1;i++){
            potences[i] = receivedData[i+11];//串口接收回来的数据，第12个为第1个观测点强度。
            ///*****
            //datas[i-1].potence=receivedData[i+11];
            ///*****
        }
   }

    //对于一次输入的一个角度值多个返回强度值，返回其直角坐标情况
    public void polarToRect(){
        //pointPotences = new PointPotence[N+1];
        for(int index = 1; index<potences.length;index++){
            //pointPotences[index] = new PointPotence();
            double radian = Math.toRadians(Parameters.angle);
            pointPotences[index].x = index*Math.cos(radian)*radius;
            pointPotences[index].y = index*Math.sin(radian)*radius;
            pointPotences[index].potence = potences[index];
            ///*****
            datas[index-1].x=(int)(index*Math.cos(radian)*radius);
            datas[index-1].y=(int)(index*Math.sin(radian)*radius);
            datas[index-1].potence=potences[index-1];
            ///*****
        }
    }

    //栅格化
    public void rasterize(){
        for(int i=1;i<N+1;i++){
            //边缘化处理
            int x = Parameters.RANGE+(int)pointPotences[i].x;
            if(x==2*Parameters.RANGE)x=2*Parameters.RANGE-1;
            int y = Parameters.RANGE+(int)pointPotences[i].y;
            if(y==2*Parameters.RANGE)y=2*Parameters.RANGE-1;
            mapContent[x][y].potence+=pointPotences[i].potence;
            mapContent[x][y].count++;
            mapContent[x][y].revoked = true;
        }
    }

    //每次栅格化完毕后，进行均值处理
    //public void process(){
    //    int mapLength = 2*Parameters.RANGE;
    //    for(int i=0;i<mapLength;i++){
    //        for(int j=0;j<mapLength;j++){
    //            if(mapContent[i][j].count>0){
    //                mapContent[i][j].calMeanValue();
    //            }
    //        }
    //    }
    //}

    //当角度为第一次的角度时，MapContent数据清空
    public void updateMapContent(){
        if(Parameters.angle==firstAngle&&!first){
            long before = System.currentTimeMillis();
            //mapContent = new MapContent[2*Parameters.RANGE][2*Parameters.RANGE];
            for(int i=0;i<2*Parameters.RANGE;i++){
                for(int j=0;j<2*Parameters.RANGE;j++){
                    mapContent[i][j].count=0;
                    mapContent[i][j].potence=0;
                    mapContent[i][j].revoked=false;
                }
            }
            long after = System.currentTimeMillis();
//            System.out.println();
//            System.out.println("time2");
//            System.out.println(after-before);
        }
    }

    @Override
    public void run(){
         //long before;
         //long after;
         if(SerialThread.sonarHead){
         SerialThread serialThread=new SerialThread();
         serialThread.start();
         while(true){
         if(serialThread.ready){
             //before = (new Date()).getTime();
             this.receivedData = new int[Parameters.FRAME];
                this.receivedData =serialThread.readBufferInt;             
             this.initiate();
             this.polarToRect();
             this.rasterize();
             serialThread.ready = false;
             Parameters.drawReady = true;
             this.updateMapContent();
             //System.out.println(firstAngle);
             //after = (new Date()).getTime();
         }
      else{
            try {
                Thread.currentThread().sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(PolarCoordinateToRect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
 }
    }
}
