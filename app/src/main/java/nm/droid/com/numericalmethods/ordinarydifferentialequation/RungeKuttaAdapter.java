package nm.droid.com.numericalmethods.ordinarydifferentialequation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class RungeKuttaAdapter extends RecyclerView.Adapter<RungeKuttaAdapter.ViewHolder> {

    private List<OrdinaryDifferentialRow> items;
    private Context context;

    RungeKuttaAdapter(Context context, List<OrdinaryDifferentialRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RungeKuttaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_runge_kutta, parent, false);
        return new RungeKuttaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RungeKuttaAdapter.ViewHolder holder, int position) {
        OrdinaryDifferentialRow ordinaryDifferentialRow = items.get(position);

        holder.iterationTextView.setText(ordinaryDifferentialRow.mIteration);
        holder.XTextView.setText(ordinaryDifferentialRow.mX);
        holder.K1TextView.setText(ordinaryDifferentialRow.mK1);
        holder.K2TextView.setText(ordinaryDifferentialRow.mK2);
        holder.K3TextView.setText(ordinaryDifferentialRow.mK3);
        holder.K4TextView.setText(ordinaryDifferentialRow.mK4);
        holder.YTextView.setText(ordinaryDifferentialRow.mY);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView iterationTextView;
        TextView XTextView;
        TextView K1TextView;
        TextView K2TextView;
        TextView K3TextView;
        TextView K4TextView;
        TextView YTextView;

        ViewHolder(View itemView) {
            super(itemView);

            iterationTextView = itemView.findViewById(R.id.iteration);
            XTextView = itemView.findViewById(R.id.X);
            K1TextView = itemView.findViewById(R.id.K1);
            K2TextView = itemView.findViewById(R.id.K2);
            K3TextView = itemView.findViewById(R.id.K3);
            K4TextView = itemView.findViewById(R.id.K4);
            YTextView = itemView.findViewById(R.id.Y);
        }
    }
}
