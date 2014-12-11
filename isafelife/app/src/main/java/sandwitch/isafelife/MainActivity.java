package sandwitch.isafelife;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import sandwitch.isafelife.services.InterpretBackgroundService;


public class MainActivity extends ActionBarActivity {

    private int interpretInterval = 10000; // 10 sec for each poll
    private Handler interpretHandler; // handler for starting background threads

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interpretHandler = new Handler();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: RASMUS - gør som jeg har gjort under med en handler, runnable og service
    // Handle event called when the toggle button for accelerometer has changed
    public void accelerate(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            // Enable sensing
        } else {
            // Disable sensing
        }
    }


    // Handle event called when the toggle button for interpret has changed
    public void interpret(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            // Enable sensing
            startInterpreting();
        } else {
            // Disable sensing
            stopInterpreting();
        }
    }

    private void createInterpretation(){
        Intent intent = new Intent(this, InterpretBackgroundService.class);
    }

    // created as a task to avoid stopping the UI thread
    Runnable interpretTask = new Runnable() {
        @Override
        public void run() {
            createInterpretation();
            interpretHandler.postDelayed(interpretTask, interpretInterval);
        }
    };

    // start the task to run forever
    private void startInterpreting(){
        interpretTask.run();
    }

    // stops the task
    private void stopInterpreting(){
        interpretHandler.removeCallbacks(interpretTask);
    }
}
