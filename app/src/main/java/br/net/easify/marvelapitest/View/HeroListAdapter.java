package br.net.easify.marvelapitest.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.net.easify.marvelapitest.Model.Character;
import br.net.easify.marvelapitest.Model.DataFactory;
import br.net.easify.marvelapitest.R;
import br.net.easify.marvelapitest.interfaces.ICharacterDelegate;

public class HeroListAdapter extends RecyclerView.Adapter<HeroListAdapter.DataHolder> implements ICharacterDelegate {
    private ICharacterDelegate delegate;
    public List<Character> characterList = new ArrayList<>();

    public HeroListAdapter(ICharacterDelegate delegate) {
        this.delegate = delegate;
        DataFactory.sharedInstance().getCharacters(this);
    }

    public void getMoreCharacters() {
        DataFactory.sharedInstance().getMoreCharacters(this);
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hero_row, parent, false);
        return new HeroListAdapter.DataHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        Character character = this.characterList.get(position);

        holder.name.setText(character.getName());
        holder.description.setText(character.getDescription());
        String fileUrl = character.getThumbnail();
        Picasso.get().load(fileUrl).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }

    @Override
    public void onGetCharacters(boolean success) {
        if ( success ) {
            this.characterList = DataFactory.sharedInstance().getCharacterList();
            this.delegate.onGetCharacters(true);
        } else {
            this.delegate.onGetCharacters(false);
        }
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        ImageView image;

        public DataHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
        }
    }
}
