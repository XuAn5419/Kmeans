package p;
import java.io.BufferedReader;  
import java.io.FileNotFoundException;  
import java.io.FileReader;  
import java.io.IOException;  
import java.util.Random;
  
public class KAverage {  
    private int sampleCount = 0;  
    private int dimensionCount = 0;  
    private int centerCount = 0;  
    private double[][] sampleValues;  
    private double[][] centers;  
    private double[][] tmpCenters;  
    private String dataFile = "";  
  
    /** 
     * 通过构造器传入数据文件 
     */  
    public KAverage(String dataFile) throws NumberInvalieException {  
        this.dataFile = dataFile;  
    }  
  
    /** 
     * 第一行为s;d;c含义分别为样例的数目，每个样例特征的维数，聚类中心个数 文件格式为d[,d]...;d[,d]... 如：1,2;2,3;1,5 
     * 每一维之间用,隔开，每个样例间用;隔开。结尾没有';' 可以有多行 
     */  
  
    private int initData(String fileName) {  
        String line;  
        String samplesValue[];  
        String dimensionsValue[] = new String[dimensionCount];  
        BufferedReader in;  
        try {  
            in = new BufferedReader(new FileReader(fileName));  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            return -1;  
        }  
        /* 
         * 预处理样本，允许后面几维为0时，不写入文件 
         */  
        for (int i = 0; i < sampleCount; i++) {  
            for (int j = 0; j < dimensionCount; j++) {  
                sampleValues[i][j] = 0;  
            }  
        }  
  
        int i = 0;  
        double tmpValue = 0.0;  
        try {  
            line = in.readLine();  
            String params[] = line.split(";");  
            if (params.length != 3) {// 必须为3个参数，否则错误  
                return -1;  
            }  
            /** 
             * 获取参数 
             */  
            this.sampleCount = Integer.parseInt(params[0]);  
            this.dimensionCount = Integer.parseInt(params[1]);  
            this.centerCount = Integer.parseInt(params[2]);  
            if (sampleCount <= 0 || dimensionCount <= 0 || centerCount <= 0) {  
                throw new NumberInvalieException("input number <= 0.");  
            }  
            if (sampleCount < centerCount) {  
                throw new NumberInvalieException(  
                        "sample number < center number");  
            }  
  
            sampleValues = new double[sampleCount][dimensionCount + 1];  
            centers = new double[centerCount][dimensionCount];  
            tmpCenters = new double[centerCount][dimensionCount];  
  
            while ((line = in.readLine()) != null) {  
                samplesValue = line.split(";");  
                for (int j = 0; j < samplesValue.length; j++) {  
                    dimensionsValue = samplesValue[j].split(",");  
                    for (int k = 0; k < dimensionsValue.length; k++) {  
                        tmpValue = Double.parseDouble(dimensionsValue[k]);  
                        sampleValues[i][k] = tmpValue;  
                    }  
                    i++;  
                }  
            }  
  
        } catch (IOException e) {  
            e.printStackTrace();  
            return -2;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return -3;  
        }  
        return 1;  
    }  
  
    /** 
     * 返回样本中第s1个和第s2个间的欧式距离 
      
    private double getDistance(int s1, int s2) throws NumberInvalieException {  
        double distance = 0.0;  
        if (s1 < 0 || s1 >= sampleCount || s2 < 0 || s2 >= sampleCount) {  
            throw new NumberInvalieException("number out of bound.");  
        }  
        for (int i = 0; i < dimensionCount; i++) {  
            distance += (sampleValues[s1][i] - sampleValues[s2][i])  
                    * (sampleValues[s1][i] - sampleValues[s2][i]);  
        }  
  
        return distance;  
    }  
    */ 
    /** 
     * 返回给定两个向量间的欧式距离 
     */  
    private double getDistance(double s1[], double s2[]) {  
        double distance = 0.0;  
        for (int i = 0; i < dimensionCount; i++) {  
            distance += (s1[i] - s2[i]) * (s1[i] - s2[i]);  
        }  
        return distance;  
    }  
  
    /** 
     * 更新样本中第s个样本的最近中心 
     */  
    private int getNearestCenter(int s) {  
        int center = 0;  
        double minDistance = Double.MAX_VALUE;  
        double distance = 0.0;  
        for (int i = 0; i < centerCount; i++) {  
            distance = getDistance(sampleValues[s], centers[i]);  
            if (distance < minDistance) {  
                minDistance = distance;  
                center = i;  
            }  
        }  
        sampleValues[s][dimensionCount] = center;  
        return center;  
    }  
  
    /** 
     * 更新所有中心 
     */  
    private void updateCenters() {  
        double center[] = new double[dimensionCount];  
        
        int count = 0;  
        for (int i = 0; i < centerCount; i++) {  
            count = 0;  
            for (int j = 0; j < sampleCount; j++) {  
                if (sampleValues[j][dimensionCount] == i) {  
                    count++;  
                    for (int k = 0; k < dimensionCount; k++) {  
                        center[k] += sampleValues[j][k];  
                    }  
                }  
            }  
            for (int j = 0; j < dimensionCount; j++) {  
                centers[i][j] = center[j] / count;  
            } 
            for (int m = 0; m < dimensionCount; m++) {  
                center[m] = 0;  
            }  
        }  
    }  
  
    /** 
     * 判断算法是否终止 
     */  
    private boolean toBeContinued() {  
        for (int i = 0; i < centerCount; i++) {  
            for (int j = 0; j < dimensionCount; j++) {  
                if (tmpCenters[i][j] != centers[i][j]) {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 关键方法，调用其他方法，处理数据 
     */  
    public void doCaculate() {  
        initData(dataFile);  
        initCenters();
        for (int i = 0; i < centerCount; i++) {  
            for (int j = 0; j < dimensionCount; j++) {  
                tmpCenters[i][j] = 0;  
            }  
        }  
        while (toBeContinued()) {  
            for (int i = 0; i < sampleCount; i++) {  
                getNearestCenter(i);  
            }  
            for (int i = 0; i < centerCount; i++) {  
                for (int j = 0; j < dimensionCount; j++) {  
                    tmpCenters[i][j] = centers[i][j];  
                }  
            }  
            updateCenters();  
            System.out  
                    .println("******************************************************");  
            showResultData();  
        }  
    }  
  
    private void initCenters() {  
        Random rand = new Random();
    	boolean[]  bool = new boolean[sampleValues.length];
    	int index;//索引第几个样本
    	
    	 for (int i = 0; i < centerCount; i++) {    	
    		 do {											//为使生成的数不重复
                 index  = rand.nextInt(sampleValues.length);
                }while(bool[index]);
             bool[index] = true;							//为使生成的数不重复
         	
         	for(int k=0;k<dimensionCount;k++)
         	{
         		centers[i][k]=sampleValues[index][k];
         	}
         }   
	}

	/* 
     * 显示数据 
     */  
    private void showSampleData() {  
        for (int i = 0; i < sampleCount; i++) {  
            for (int j = 0; j < dimensionCount; j++) {  
                if (j == 0) {  
                    System.out.print(sampleValues[i][j]);  
                } else {  
                    System.out.print("," + sampleValues[i][j]);  
                }  
            }  
            System.out.println();  
        }  
    }  
  
    /* 
     * 分组显示结果 
     */  
    private void showResultData() {  
        for (int i = 0; i < centerCount; i++) {  
            System.out.println("第" + (i + 1) + "个分组内容为:");  
            for (int j = 0; j < sampleCount; j++) {  
                if (sampleValues[j][dimensionCount] == i) {  
                    for (int k = 0; k <= dimensionCount; k++) {  
                        if (k == 0) {  
                            System.out.print(sampleValues[j][k]);  
                        } else {  
                            System.out.print("," + sampleValues[j][k]);  
                        }  
                    }  
                    System.out.println();  
                }  
            }  
        }  
    }  
    /*
     * 显示数据
     */
    private void showData() { 
    	System.out.println("***********显示数据***********");
        for(int i=0;i<sampleCount;i++)
        {
        	for(int j=0;j<=dimensionCount;j++)
        	{
        		System.out.print(sampleValues[i][j]+"\t");
        		
        	}
        	System.out.println();
        }
    }  
    public static void main(String[] args) {  
        /* 
         *也可以通过命令行得到参数 
         */  
        String fileName = "C:\\Users\\XuAn\\Desktop\\K\\src\\p\\sample.txt";  
        if(args.length > 0){  
            fileName = args[0];  
        }  
          
        try {  
            KAverage ka = new KAverage(fileName);  
            ka.doCaculate();  
            System.out.println("***************************<<result>>**************************");  
            ka.showResultData();  
            ka.showData();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
} 