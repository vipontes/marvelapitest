package br.net.easify.marvelapitest.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.net.easify.marvelapitest.Model.Character;
import br.net.easify.marvelapitest.R;

public class DetailActivity extends AppCompatActivity {

    private Character character;
    private ImageView image;
    private TextView name;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Character character = (Character) getIntent().getSerializableExtra("character");
        this.character = character;

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);

        String fileUrl = character.getThumbnail();
        Picasso.get().load(fileUrl).into(image);

        name.setText(character.getName());
        description.setText(character.getDescription());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
