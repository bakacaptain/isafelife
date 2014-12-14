package sandwitch.isafelife.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.security.spec.EncodedKeySpec;

/**
 * Created by Baka on 14-12-2014.
 */
public class LogWriter {

    private Context context;
    private String fileName;
    private File root;
    private File dir;

    public LogWriter(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        dir = context.getFilesDir();
        this.root = new File(dir,"");
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
            Log.i("write to file","Check dir exists:"+dir);
            File file = new File(dir, fileName);
            if(!file.exists()){
                file.createNewFile();
            }
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
