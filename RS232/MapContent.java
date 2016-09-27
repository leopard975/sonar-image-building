package RS232;

/**
 *
 * @author afunx
 */
public class MapContent {
    public int count = 0;//累计数量
    public int potence = 0;//累计强度
    public boolean revoked = false;//该单元是否被激活

    @Override
    public String toString(){
        return("count= "+ count +" potence= " + potence);
    }

    //计算均值
    public int calMeanValue(){
            return((potence / count));
    }
}
