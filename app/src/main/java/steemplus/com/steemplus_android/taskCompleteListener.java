package steemplus.com.steemplus_android;

public interface taskCompleteListener<T>
{
    void onTaskComplete(T result, String action);
}
