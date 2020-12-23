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
     * ͨ�����������������ļ� 
     */  
    public KAverage(String dataFile) throws NumberInvalieException {  
        this.dataFile = dataFile;  
    }  
  
    /** 
     * ��һ��Ϊs;d;c����ֱ�Ϊ��������Ŀ��ÿ������������ά�����������ĸ��� �ļ���ʽΪd[,d]...;d[,d]... �磺1,2;2,3;1,5 
     * ÿһά֮����,������ÿ����������;��������βû��';' �����ж��� 
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
         * Ԥ����������������漸άΪ0ʱ����д���ļ� 
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
            if (params.length != 3) {// ����Ϊ3���������������  
                return -1;  
            }  
            /** 
             * ��ȡ���� 
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
     * ���������е�s1���͵�s2�����ŷʽ���� 
      
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
     * ���ظ��������������ŷʽ���� 
     */  
    private double getDistance(double s1[], double s2[]) {  
        double distance = 0.0;  
        for (int i = 0; i < dimensionCount; i++) {  
            distance += (s1[i] - s2[i]) * (s1[i] - s2[i]);  
        }  
        return distance;  
    }  
  
    /** 
     * ���������е�s��������������� 
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
     * ������������ 
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
     * �ж��㷨�Ƿ���ֹ 
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
     * �ؼ����������������������������� 
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
    	int index;//�����ڼ�������
    	
    	 for (int i = 0; i < centerCount; i++) {    	
    		 do {											//Ϊʹ���ɵ������ظ�
                 index  = rand.nextInt(sampleValues.length);
                }while(bool[index]);
             bool[index] = true;							//Ϊʹ���ɵ������ظ�
         	
         	for(int k=0;k<dimensionCount;k++)
         	{
         		centers[i][k]=sampleValues[index][k];
         	}
         }   
	}

	/* 
     * ��ʾ���� 
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
     * ������ʾ��� 
     */  
    private void showResultData() {  
        for (int i = 0; i < centerCount; i++) {  
            System.out.println("��" + (i + 1) + "����������Ϊ:");  
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
     * ��ʾ����
     */
    private void showData() { 
    	System.out.println("***********��ʾ����***********");
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
         *Ҳ����ͨ�������еõ����� 
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