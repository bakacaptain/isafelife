package sandwitch.isafelife.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by Baka on 14-12-2014.
 */
public class LogWriter {

    private Context context;
    private String fileName;
    private File root;

    public LogWriter(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        this.root = new File(Environment.getExternalStorageDirectory(),null);
        if(!root.exists()){
            root.mkdirs();
        }
    }

    /**
     * Write to a file by appending to the end of it.
     * @param text
     */
    public void write(String text){
        write(text,false);
    }

    /**
     * Write to a file by either overriding the file with a new one or appending to the end of it.
     * @param text
     * @param override
     */
    public void write(String text, boolean override){
        try {
            File file = new File(root, fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(text);
            writer.flush();
            writer.close();
            Log.i("write to file","Successfully wrote to file");
        }catch (Exception e){
            Log.e("write to file",e.getMessage(),e);
        }
    }


}
