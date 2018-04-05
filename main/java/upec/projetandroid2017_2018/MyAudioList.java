package upec.projetandroid2017_2018;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAudioList extends AppCompatActivity {


    ListView listView;
    TextView txt, titre, duree, time;
    SeekBar sb;
    EditText searchfilter;
    ImageButton play, suivant, precedant, retour;
    //SearchView sv;

    AlertDialog.Builder builder;
    AlertDialog.Builder builderD;
    AlertDialog.Builder builderR;

    private MyAdapter adapter;
    private List<String> songs;


    MediaPlayer mp = new MediaPlayer();
    // Handler handler;
    // Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_audio_list);

        txt = findViewById(R.id.txtv);
        listView = findViewById(R.id.list);
        builder = new AlertDialog.Builder(MyAudioList.this);
        builderD = new AlertDialog.Builder(MyAudioList.this);
        builderR = new AlertDialog.Builder(MyAudioList.this);

        titre = findViewById(R.id.titre);
        duree = findViewById(R.id.duree);
        time = findViewById(R.id.time); //gauche
        play = findViewById(R.id.playb);//droite
        precedant = findViewById(R.id.prec);
        suivant = findViewById(R.id.suiv);
        sb = findViewById(R.id.sb);
        retour = findViewById(R.id.retour);
        searchfilter = findViewById(R.id.research);

      // sv =findViewById(R.id.search);

        final String[] items = {"Delete", "Rename"};

        songs = new ArrayList<>();

        updatePlayList();
        searchfilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (MyAudioList.this).adapter.getFilter().filter(charSequence);
                //filter((String) charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

               //filter(editable.toString());
            }
        });
       /* sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });*/
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp.isPlaying()) mp.pause();
                play.setImageResource(R.drawable.play_c);
                Toast.makeText(getApplicationContext(), "dictaphone", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyAudioList.this, MainActivity.class);
                String EXTRA_MESSAGE = "record";
                intent.putExtra(EXTRA_MESSAGE, "record");
                startActivity(intent);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mp.isPlaying() && mp != null) {
                    mp.start();
                    play.setImageResource(R.drawable.pause_c);
                } else {
                    mp.pause();
                    play.setImageResource(R.drawable.play_c);
                }
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp.seekTo(progress);
                    sb.setProgress(progress);

                    //Toast.makeText(getApplicationContext(), sb.getProgress()+" = "+mp.getDuration(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(50);

                    } catch (InterruptedException e) {

                    }
                }


            }

        }).start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {


                    play.setImageResource(R.drawable.pause_c);
                    mp.reset();
                    mp.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/AudioRecorder/" + songs.get(i));
                    mp.prepare();
                    sb.setMax(mp.getDuration());
                    mp.start();
                    // duree.setText(Integer.toString(mp.getDuration() / 60000) + ":" + mp.getDuration() / 1000);
                    titre.setText(songs.get(i));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/AudioRecorder/" + songs.get(pos));

                if (file != null && file.exists()) {

                    builder.setTitle("Choice ...").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {

                            switch (i) {
                                case 0:
                                    builderD.setTitle("Are you sur to delete ?");
                                    // Add the buttons
                                    builderD.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User clicked OK button
                                            file.delete();
                                            Toast.makeText(getApplicationContext(), songs.get(pos) + " supprimé !", Toast.LENGTH_SHORT).show();
                                            adapter.remove(songs.get(pos));
                                            //songs.set(i,Environment.getExternalStorageDirectory().getPath()+"/AudioRecorder"+"/moha.3gp");
                                            adapter.notifyDataSetChanged();
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builderD.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                            Toast.makeText(getApplicationContext(), " Cancel !", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    // Set other dialog properties


                                    // Create the AlertDialog
                                    builderD.create().show();

                                    break;
                                case 1:
                                    builderR.setTitle("rename ...");
                                    View v = getLayoutInflater().inflate(R.layout.renamefile, null);
                                    final EditText name = (EditText) v.findViewById(R.id.namer);
                                    Button cancel = (Button) v.findViewById(R.id.canceler);
                                    Button rename = (Button) v.findViewById(R.id.save);
                                    name.setText(songs.get(pos));
                                    builderR.setView(v);

                                    rename.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            songs.set(pos, name.getText() + ".3gp");
                                            file.renameTo(new File(Environment.getExternalStorageDirectory().getPath() + "/AudioRecorder/" + name.getText() + ".3gp"));
                                            Toast.makeText(getApplicationContext(), name.getText(), Toast.LENGTH_SHORT).show();
                                            adapter.notifyDataSetChanged();
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();

                                        }
                                    });

                                    builderR.create().show();
                                    dialogInterface.dismiss();

                                    break;
                            }

                        }
                    }); ///fin builder

                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog

                            Toast.makeText(getApplicationContext(), " Cancel !", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();

                    return true;

                }

                Toast.makeText(getApplicationContext(), " Erreur !", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    private void filter(String text) {
        List<String> newlistfiltered = new ArrayList<>();
        for(String item : songs){
            if(item.toLowerCase().contains(text.toLowerCase())){
                newlistfiltered.add(item);
            }
        }
        adapter.filterList(newlistfiltered);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int currentPosition = msg.what;
            sb.setProgress(currentPosition);//
            if (mp.isPlaying()) {
                time.setText(refreshTime(currentPosition));
                duree.setText("-" + refreshTime(mp.getDuration() - currentPosition));
            }
            if (currentPosition >= mp.getDuration() - 50) play.setImageResource(R.drawable.play_c);

        }
    };

    public String refreshTime(int t) {
        String timer = " ";
        int min = t / 1000 / 60;
        int sec = t / 1000 % 60;

        timer = min + ":";
        if (sec < 10) timer += "0";
        timer += sec;

        return timer;
    }


    public void updatePlayList() {
        File home = new File(Environment.getExternalStorageDirectory().getPath() + "/AudioRecorder");

        if (home.listFiles().length > 0) {
            for (File file : home.listFiles()) {
                songs.add(file.getName());
            }
            adapter = new MyAdapter(this, songs, R.drawable.img1);
            listView.setAdapter(adapter);  //setListAdapter(adapter)

        }


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        MenuItem item = menu.findItem(R.id.search_item);
        SearchView searchView =(SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //adapter = new MyAdapter(MyAudioList.this, songs, R.drawable.img1);
               // adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }*/
}
