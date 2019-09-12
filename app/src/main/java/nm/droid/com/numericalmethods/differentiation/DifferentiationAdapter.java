package nm.droid.com.numericalmethods.differentiation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class DifferentiationAdapter extends RecyclerView.Adapter<DifferentiationAdapter.ViewHolder> {

    private List<DifferentiationRow> items;
    private Context context;
    private int enteredValues;

    DifferentiationAdapter(Context context, List<DifferentiationRow> items, int enteredValues) {
        this.context = context;
        this.items = items;
        this.enteredValues = enteredValues;
    }

    @Override
    public DifferentiationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int[] interpolationRowLayouts = {R.layout.row_interpolation_four, R.layout.row_interpolation_five,
                                        R.layout.row_interpolation_six, R.layout.row_interpolation_seven};
        View view = LayoutInflater.from(parent.getContext())
                .inflate(interpolationRowLayouts[enteredValues - 4], parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DifferentiationAdapter.ViewHolder holder, int position) {
        DifferentiationRow differentiationRow = items.get(position);

        holder.headerTextView.setText(differentiationRow.mHeader);
        for (int i = 0; i < enteredValues; i++) {
            holder.values[i].setText(differentiationRow.mValues[i]);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView headerTextView;
        TextView[] values;

        ViewHolder(View itemView) {
            super(itemView);

            headerTextView = itemView.findViewById(R.id.header);
            values = new TextView[enteredValues];

            switch (enteredValues) {
                case 4:
                    values[0] = itemView.findViewById(R.id.value_one);
                    values[1] = itemView.findViewById(R.id.value_two);
                    values[2] = itemView.findViewById(R.id.value_three);
                    values[3] = itemView.findViewById(R.id.value_four);
                    break;

                case 5:
                    values[0] = itemView.findViewById(R.id.value_one);
                    values[1] = itemView.findViewById(R.id.value_two);
                    values[2] = itemView.findViewById(R.id.value_three);
                    values[3] = itemView.findViewById(R.id.value_four);
                    values[4] = itemView.findViewById(R.id.value_five);
                    break;

                case 6:
                    values[0] = itemView.findViewById(R.id.value_one);
                    values[1] = itemView.findViewById(R.id.value_two);
                    values[2] = itemView.findViewById(R.id.value_three);
                    values[3] = itemView.findViewById(R.id.value_four);
                    values[4] = itemView.findViewById(R.id.value_five);
                    values[5] = itemView.findViewById(R.id.value_six);
                    break;

                case 7:
                    values[0] = itemView.findViewById(R.id.value_one);
                    values[1] = itemView.findViewById(R.id.value_two);
                    values[2] = itemView.findViewById(R.id.value_three);
                    values[3] = itemView.findViewById(R.id.value_four);
                    values[4] = itemView.findViewById(R.id.value_five);
                    values[5] = itemView.findViewById(R.id.value_six);
                    values[6] = itemView.findViewById(R.id.value_seven);
                    break;
            }
        }
    }
}
