package nm.droid.com.numericalmethods.powermatrix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class PowerThreeAdapter extends RecyclerView.Adapter<PowerThreeAdapter.ViewHolder> {

    private List<PowerRow> items;
    private Context context;

    PowerThreeAdapter(Context context, List<PowerRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public PowerThreeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_power_three, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PowerThreeAdapter.ViewHolder holder, int position) {
        PowerRow powerRow = items.get(position);

        holder.iterationTextView.setText(powerRow.mIteration);
        for (int i = 0; i < 9; i++) {
            holder.innerRowZero[i].setText(powerRow.mInnerRowZero[i]);
            holder.innerRowOne[i].setText(powerRow.mInnerRowOne[i]);
            holder.innerRowTwo[i].setText(powerRow.mInnerRowTwo[i]);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView iterationTextView;
        TextView[] innerRowZero = new TextView[9];
        TextView[] innerRowOne = new TextView[9];
        TextView[] innerRowTwo = new TextView[9];

        ViewHolder(View itemView) {
            super(itemView);

            iterationTextView = itemView.findViewById(R.id.iteration);
            innerRowZero[0] = itemView.findViewById(R.id.A00);
            innerRowZero[1] = itemView.findViewById(R.id.A01);
            innerRowZero[2] = itemView.findViewById(R.id.A02);
            innerRowZero[3] = itemView.findViewById(R.id.space_or_x_0);
            innerRowZero[4] = itemView.findViewById(R.id.X00);
            innerRowZero[5] = itemView.findViewById(R.id.space_or_equals_0);
            innerRowZero[6] = itemView.findViewById(R.id.space_or_eigen_0);
            innerRowZero[7] = itemView.findViewById(R.id.space_or_cross_0);
            innerRowZero[8] = itemView.findViewById(R.id.XBar00);
            innerRowOne[0] = itemView.findViewById(R.id.A10);
            innerRowOne[1] = itemView.findViewById(R.id.A11);
            innerRowOne[2] = itemView.findViewById(R.id.A12);
            innerRowOne[3] = itemView.findViewById(R.id.space_or_x_1);
            innerRowOne[4] = itemView.findViewById(R.id.X10);
            innerRowOne[5] = itemView.findViewById(R.id.space_or_equals_1);
            innerRowOne[6] = itemView.findViewById(R.id.space_or_eigen_1);
            innerRowOne[7] = itemView.findViewById(R.id.space_or_cross_1);
            innerRowOne[8] = itemView.findViewById(R.id.XBar10);
            innerRowTwo[0] = itemView.findViewById(R.id.A20);
            innerRowTwo[1] = itemView.findViewById(R.id.A21);
            innerRowTwo[2] = itemView.findViewById(R.id.A22);
            innerRowTwo[3] = itemView.findViewById(R.id.space_or_x_2);
            innerRowTwo[4] = itemView.findViewById(R.id.X20);
            innerRowTwo[5] = itemView.findViewById(R.id.space_or_equals_2);
            innerRowTwo[6] = itemView.findViewById(R.id.space_or_eigen_2);
            innerRowTwo[7] = itemView.findViewById(R.id.space_or_cross_2);
            innerRowTwo[8] = itemView.findViewById(R.id.XBar20);
        }
    }
}
