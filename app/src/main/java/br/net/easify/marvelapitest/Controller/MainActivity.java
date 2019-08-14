package br.net.easify.marvelapitest.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import br.net.easify.marvelapitest.Model.Character;
import br.net.easify.marvelapitest.Model.DataFactory;
import br.net.easify.marvelapitest.R;
import br.net.easify.marvelapitest.View.HeroListAdapter;
import br.net.easify.marvelapitest.interfaces.ICharacterDelegate;

public class MainActivity extends AppCompatActivity implements ICharacterDelegate {

    private ProgressDialog progress;
    private RecyclerView recyclerView;
    private HeroListAdapter adapter;
    private LinearLayoutManager manager;
    private GestureDetector gestureDetector;
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa o singleton
        DataFactory.sharedInstance().setContext(this);

        this.progress = new ProgressDialog(this);

        recyclerView = findViewById(R.id.heroList);
        adapter = new HeroListAdapter(this);
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = manager.findLastVisibleItemPosition();
                int maxPosition = adapter.getItemCount() - 1;

                if (lastvisibleitemposition == maxPosition) {
                    if ( !loading ) {
                        progress.setMessage(getString(R.string.loading));
                        progress.show();
                        loading = true;
                        adapter.getMoreCharacters();
                    }
                }

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                return (view != null && gestureDetector.onTouchEvent(motionEvent));
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        gestureDetector = new GestureDetector(
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        final int position = recyclerView.getChildAdapterPosition(view);

                        Character character = DataFactory.sharedInstance().getCharacter(position);

                        Intent i = new Intent(MainActivity.this, DetailActivity.class);
                        i.putExtra("character", character);
                        startActivity(i);

                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {


                    }
                });
    }

    @Override
    public void onGetCharacters(boolean success) {
        if (this.progress.isShowing()) {
            this.progress.dismiss();
        }

        if ( success ) {
            if ( adapter != null ) {
                adapter.notifyDataSetChanged();
            }
            loading = false;
        } else {
            Toast.makeText(this, getString(R.string.load_data_error), Toast.LENGTH_LONG).show();
        }
    }
}
