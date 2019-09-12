package nm.droid.com.numericalmethods.ordinarydifferentialequation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class HeunAdapter extends RecyclerView.Adapter<HeunAdapter.ViewHolder> {

    private List<OrdinaryDifferentialRow> items;
    private Context context;

    HeunAdapter(Context context, List<OrdinaryDifferentialRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public HeunAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_heun, parent, false);
        return new HeunAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeunAdapter.ViewHolder holder, int position) {
        OrdinaryDifferentialRow ordinaryDifferentialRow = items.get(position);

        holder.iterationTextView.setText(ordinaryDifferentialRow.mIteration);
        holder.XTextView.setText(ordinaryDifferentialRow.mX);
        holder.YStarTextView.setText(ordinaryDifferentialRow.mYStar);
        holder.YTextView.setText(ordinaryDifferentialRow.mY);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView iterationTextView;
        TextView XTextView;
        TextView YStarTextView;
        TextView YTextView;

        ViewHolder(View itemView) {
            super(itemView);

            iterationTextView = itemView.findViewById(R.id.iteration);
            XTextView = itemView.findViewById(R.id.X);
            YStarTextView = itemView.findViewById(R.id.YStar);
            YTextView = itemView.findViewById(R.id.Y);
        }
    }
}
