package sandwitch.isafelife.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.security.spec.EncodedKeySpec;

/**
 * Created by Baka on 14-12-2014.
 */
public class LogWriter {

    public static void write(File file, String text){
        try {
            if(file == null && file.canWrite()){
                Log.w("write file","File cannot be written to.");
                return;
            }

            boolean shouldRewrite = true;

            file.createNewFile();
            Writer out = new BufferedWriter(new FileWriter(file,shouldRewrite));
            out.write(text);
            out.close();

            Log.i("write file","Successfully wrote to file");
        }catch (Exception e){
            Log.e("write file",e.getMessage(),e);
        }
    }
}
