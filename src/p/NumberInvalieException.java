package p;

/*  
 * 根据自己的需要定义一些异常，使得系统性更强  
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