package helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asdev.naa.R;

import java.util.List;

public class noteListAdapter extends
        RecyclerView.Adapter<noteViewHolder> {

    private List<noteInfo> noteList;
    private Context context;

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View noteView =  LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card, parent, false);
        return new noteViewHolder(noteView);
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
