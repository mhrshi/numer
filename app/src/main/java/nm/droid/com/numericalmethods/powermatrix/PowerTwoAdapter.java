package nm.droid.com.numericalmethods.powermatrix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class PowerTwoAdapter extends RecyclerView.Adapter<PowerTwoAdapter.ViewHolder> {

    private List<PowerRow> items;
    private Context context;

    PowerTwoAdapter(Context context, List<PowerRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public PowerTwoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_power_two, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PowerTwoAdapter.ViewHolder holder, int position) {
        PowerRow powerRow = items.get(position);

        holder.iterationTextView.setText(powerRow.mIteration);
        for (int i = 0; i < 8; i++) {
            holder.innerRowZero[i].setText(powerRow.mInnerRowZero[i]);
            holder.innerRowOne[i].setText(powerRow.mInnerRowOne[i]);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView iterationTextView;
        TextView[] innerRowZero = new TextView[8];
        TextView[] innerRowOne = new TextView[8];

        ViewHolder(View itemView) {
            super(itemView);

            iterationTextView = itemView.findViewById(R.id.iteration);
            innerRowZero[0] = itemView.findViewById(R.id.A00);
            innerRowZero[1] = itemView.findViewById(R.id.A01);
            innerRowZero[2] = itemView.findViewById(R.id.space_or_x_0);
            innerRowZero[3] = itemView.findViewById(R.id.X00);
            innerRowZero[4] = itemView.findViewById(R.id.space_or_equals_0);
            innerRowZero[5] = itemView.findViewById(R.id.space_or_eigen_0);
            innerRowZero[6] = itemView.findViewById(R.id.space_or_cross_0);
            innerRowZero[7] = itemView.findViewById(R.id.XBar00);
            innerRowOne[0] = itemView.findViewById(R.id.A10);
            innerRowOne[1] = itemView.findViewById(R.id.A11);
            innerRowOne[2] = itemView.findViewById(R.id.space_or_x_1);
            innerRowOne[3] = itemView.findViewById(R.id.X10);
            innerRowOne[4] = itemView.findViewById(R.id.space_or_equals_1);
            innerRowOne[5] = itemView.findViewById(R.id.space_or_eigen_1);
            innerRowOne[6] = itemView.findViewById(R.id.space_or_cross_1);
            innerRowOne[7] = itemView.findViewById(R.id.XBar10);
        }
    }
}
