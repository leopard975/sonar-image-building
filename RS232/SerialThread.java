package RS232;

/**
 *
 * @author afunx
 */
import java.io.*;
import java.util.Enumeration;
import java.util.TooManyListenersException;
//import javax.comm.*;
import gnu.io.*;

//RXTXcomm
public class SerialThread  extends Thread implements SerialPortEventListener {
    //public static final int N = 252;//采样点的总数
    public static final int FRAME = 265;//一帧收到的数据字节数
    private static Enumeration portList;
    private static CommPortIdentifier portId;
    //static byte[] messageBytes = {-2,68,16,10,0,0,67,0,6,1,20,60,120,3,6,0,0,0,0,25,8,6,0,0,5,100,-3};
    private static SerialPort serialPort;
    private static InputStream inputStream;//从RS232收到的SONAR数据
    private static OutputStream outputStream;//向RS232发送的SONAR命令
    //private static RandomAccessFile randomAccessFile;//**从硬盘读取inputStream数据
    private static DataInputStream dataInputStream;//**从硬盘读取inputStream数据
    private static DataOutputStream dataOutputStream;//**从硬盘保存inputStream数据
    //private static Scanner in;
    private static FileOutputStream fout;
    private static FileInputStream fin;
    private byte[] readBuffer;// = new byte[300];
    protected int[] readBufferInt;
    //int count = 0;
    public boolean ready;//数据是否全部读出
    public static boolean sonarHead;//**数据是否来自声纳
    

    public SerialThread(){
        //sonarHead = true;
        sonarHead = false;
        ready = false;
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("COM8")) {
                    try {
                        serialPort = (SerialPort)
                            portId.open("SerialThread", 2000);
                    } catch (PortInUseException e) {System.out.println("error0");}
                    try {
                        serialPort.setSerialPortParams(115200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {System.out.println("error1");}
                    try {
                        serialPort.addEventListener(this);
                    } catch (TooManyListenersException e) {System.out.println("error2");}
                    serialPort.notifyOnDataAvailable(true);
                }
            }
        }
    }

    @Override
    public void run(){
//        System.out.println(sonarHead);

        //int time = 0;//*****读取数据的次数
        //****try{
        //****    randomAccessFile = new RandomAccessFile("d:/Experiment.dat","rws");
        //****}
        //****catch(IOException exception){
        //****    exception.printStackTrace();
        //****}
        if(sonarHead){
        try{
            fout = new FileOutputStream("./gooddata20m1334.dat");//("d:/科研/声纳信号处理/声纳软件及数据/gooddata20m1334.dat");
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
        dataOutputStream = new DataOutputStream(fout);}
        else{
        try{
            fin = new FileInputStream("./gooddata20m1334.81a");//("d:/科研/声纳信号处理/声纳软件及数据/gooddata20m1334.81a");
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
        
        dataInputStream = new DataInputStream(fin);
        }
        while(true){
        readBuffer = new byte[FRAME];
        readBufferInt = new int[FRAME];
        ready = false;
        //count++;
        //*****数据来自于SONAR
        if(sonarHead){
            
            try {
                outputStream = serialPort.getOutputStream();
            }catch (IOException e) {System.out.println("error3");}
          //  try {
           //     outputStream.write(Parameters.commandBytes);
                //outputStream.write(messageBytes);
           // } catch (IOException e) {System.out.println("error4");}//\\
            try {
                outputStream.close();
            } catch (IOException e) {System.out.println("error5");}
            try {
                inputStream = serialPort.getInputStream();
            } catch (IOException e) {System.out.println("error6");}
            try {
                Thread.currentThread().sleep(70);
            } catch (InterruptedException e) {System.out.println("error8");}
            try {
                inputStream.close();
                //dataOutputStream.write(readBuffer,0,FRAME);
                //dataOutputStream.flush();
                dataOutputStream.write(readBuffer,0,FRAME);
                dataOutputStream.flush();
                for(int i=0;i<FRAME;i++){
                    readBufferInt[i] =((int)readBuffer[i])&0x00ff;
                }
                //*****
                //randomAccessFile.write(readBuffer);
                //randomAccessFile.read(originalBuffer);
                //randomAccessFile.write(originalBuffer);
                
                //randomAccessFile.close();
                //*****
                try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {System.out.println("error9");}
                ready = true;
            } catch (IOException e) {System.out.println("error7");e.printStackTrace();}
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {System.out.println("error9");}
        }
        else{//*****数据来自于硬盘
            try{
                Thread.currentThread().sleep(60);
                //dataInputStream.skip(FRAME*time);
                dataInputStream.skip(100);
                dataInputStream.read(readBuffer);
                //System.out.println(readBuffer[0]);
                //randomAccessFile.seek(time*FRAME);
                //randomAccessFile.read(readBuffer, time*FRAME, FRAME);
                //randomAccessFile.close();
                //randomAccessFile.read(readBuffer);
                for(int i=0;i<FRAME;i++){
                    readBufferInt[i] =((int)readBuffer[i])&0x00ff;
                }
                dataInputStream.skip(19);
                ready = true;
                Thread.currentThread().sleep(10);
            }
            catch (InterruptedException e) {e.printStackTrace();}
            catch(IOException exception){
                exception.printStackTrace();
            }
            //*****读入数据seek(time*FRAME)
            //time++;
        }
        //time++;
        }
    }
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            try {
                while (inputStream.available() > 0) {
                    int numBytes = inputStream.read(readBuffer);
                    //count = count + numBytes;
                }
                //************************************
            } catch (IOException e) {}
            break;
        }
    }
}
