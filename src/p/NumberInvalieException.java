package p;

/*  
 * �����Լ�����Ҫ����һЩ�쳣��ʹ��ϵͳ�Ը�ǿ  
 */  
public class NumberInvalieException extends Exception {   
    private String cause;   
       
    public NumberInvalieException(String cause){   
        if(cause == null || "".equals(cause)){   
            this.cause = "unknow";   
        }else{   
            this.cause = cause;   
        }   
    }   
    @Override  
    public String toString() {   
        return "Number Invalie!Cause by " + cause;   
    }   
}  