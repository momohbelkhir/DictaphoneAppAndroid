package upec.projetandroid2017_2018;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    //moh


    ImageButton record,play,list;
    TextView txt;


    AlertDialog.Builder builderD;
    AlertDialog.Builder builderR;


    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private static String mFileName = null;
    //private static String fileName =null;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    //private MediaRecorder recorder =null;

    private boolean permissionToRecordAccepted = false;

    private String [] permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        record = (ImageButton) findViewById(R.id.record);
        play=(ImageButton) findViewById(R.id.play);
        list=(ImageButton) findViewById(R.id.list);

        txt =findViewById(R.id.textView);


        builderD = new AlertDialog.Builder(MainActivity.this);
        builderR = new AlertDialog.Builder(MainActivity.this);

       // mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";

        //mFileName += UUID.randomUUID().toString()+"audio.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        play.setEnabled(false);


        record.setOnClickListener(new View.OnClickListener() {
            boolean mStartRecording = true;
            @Override
            public void onClick(View view) {

                onRecord(mStartRecording);

                if (mStartRecording) {
                    //record.setText("Stop recording");
                    record.setImageResource(R.drawable.microff);
                    txt.setText("Recording ... You should stop recording to play");
                } else {
                   // record.setText("Start recording");
                    record.setImageResource(R.drawable.micro);
                    txt.setText("Record pause !");
                }
                mStartRecording = !mStartRecording;


            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            boolean mStartPlaying = true;
            @Override
            public void onClick(View view) {

                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    //play.setText("Stop playing");
                    play.setImageResource(R.drawable.pause_c);
                    txt.setText("Playing ... You should stop playing to record");
                } else {
                    //
                    play.setImageResource(R.drawable.play_c);
                    txt.setText("Play pause !");
                }
                mStartPlaying = !mStartPlaying;
            }
        });

      list.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              //  if(mPlayer.isPlaying())stopPlaying();
             // if(mStartRecording)stopRecording();
              String directory;
              directory = Environment.getExternalStorageDirectory().getPath();
              File file = new File(directory,"AudioRecorder");

              if( file.exists() ) {

                  Toast.makeText(getApplicationContext(), "Record List", Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(MainActivity.this, MyAudioList.class);
                  String EXTRA_MESSAGE = "record list";
                  intent.putExtra(EXTRA_MESSAGE, "reord list");
                  startActivity(intent);
              }else{
                  Toast.makeText(getApplicationContext(), " Liste Vide !", Toast.LENGTH_SHORT).show();
              }
          }
      });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();


    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
            play.setEnabled(false);
        } else {
            stopRecording();
            play.setEnabled(true);
            //dialog
            saveORremove();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
            record.setEnabled(false);
        } else {
            stopPlaying();
            record.setEnabled(true);
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(getFilePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }
    private String getFilePath(){
        mFileName = Environment.getExternalStorageDirectory().getPath();
        File file = new File(mFileName,"AudioRecorder");
        if( ! file.exists() ) file.mkdirs();

        mFileName = (file.getAbsolutePath()+"/"+ System.currentTimeMillis()+".3gp");
        return mFileName;
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
    /*public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            return;
        }
        if (mediaPlayer != null && !mediaPlayer.isPlaying())
            mediaPlayer.start();
    }*/

 private void saveORremove(){
     final File file = new File(mFileName);

     if (file != null && file.exists()) {


                         builderD.setTitle("would you save ?");
                         // Add the buttons
                         builderD.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 // User clicked OK button
                                 builderR.setTitle("Save ...");
                                 View v = getLayoutInflater().inflate(R.layout.save, null);
                                 final EditText name = (EditText) v.findViewById(R.id.namer);
                                 Button cancel = (Button) v.findViewById(R.id.canceler);
                                 Button save = (Button) v.findViewById(R.id.save);

                                 builderR.setView(v);

                                 save.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {

                                         file.renameTo(new File(Environment.getExternalStorageDirectory().getPath() + "/AudioRecorder/" + name.getText() + ".3gp"));
                                         Toast.makeText(getApplicationContext(), name.getText(), Toast.LENGTH_SHORT).show();

                                     }
                                 });
                                 cancel.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         file.delete();
                                         Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                                         play.setEnabled(false);

                                     }
                                 });

                                 builderR.create().show();

                             }
                         });
                         builderD.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 // User cancelled the dialog
                                 file.delete();
                                 Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                                 play.setEnabled(false);

                             }
                         });
                         // Set other dialog properties


                         // Create the AlertDialog
                         builderD.create().show();





                 }







     }
 }






