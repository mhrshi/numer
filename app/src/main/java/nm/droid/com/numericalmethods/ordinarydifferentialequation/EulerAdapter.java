package nm.droid.com.numericalmethods.ordinarydifferentialequation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class EulerAdapter extends RecyclerView.Adapter<EulerAdapter.ViewHolder> {

    private List<OrdinaryDifferentialRow> items;
    private Context context;

    EulerAdapter(Context context, List<OrdinaryDifferentialRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public EulerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_euler, parent, false);
        return new EulerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EulerAdapter.ViewHolder holder, int position) {
        OrdinaryDifferentialRow ordinaryDifferentialRow = items.get(position);

        holder.iterationTextView.setText(ordinaryDifferentialRow.mIteration);
        holder.XTextView.setText(ordinaryDifferentialRow.mX);
        holder.YTextView.setText(ordinaryDifferentialRow.mY);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView iterationTextView;
        TextView XTextView;
        TextView YTextView;

        ViewHolder(View itemView) {
            super(itemView);

            iterationTextView = itemView.findViewById(R.id.iteration);
            XTextView = itemView.findViewById(R.id.X);
            YTextView = itemView.findViewById(R.id.Y);
        }
    }
}
