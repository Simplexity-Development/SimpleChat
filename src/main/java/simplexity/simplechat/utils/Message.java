package simplexity.simplechat.utils;

public enum Message {
    ERROR_PAPI_NEEDS_ARGUMENT("papi tag requires an argument");
    String message;
    
    Message(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    private void setMessage(String message) {
        this.message = message;
    }
}
