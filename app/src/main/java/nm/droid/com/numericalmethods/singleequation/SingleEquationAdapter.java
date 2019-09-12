package nm.droid.com.numericalmethods.singleequation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class SingleEquationAdapter extends RecyclerView.Adapter<SingleEquationAdapter.ViewHolder> {

    private List<SingleEquationRow> items;
    private Context context;

    SingleEquationAdapter(Context context, List<SingleEquationRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public SingleEquationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_single_equation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleEquationAdapter.ViewHolder holder, int position) {
        SingleEquationRow singleEquationRow = items.get(position);

        holder.iterationTextView.setText(singleEquationRow.mIteration);
        holder.LEPTextView.setText(singleEquationRow.mLEP);
        holder.REPTextView.setText(singleEquationRow.mREP);
        holder.XTextView.setText(singleEquationRow.mX);
        holder.FXTextView.setText(singleEquationRow.mFX);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView iterationTextView;
        TextView LEPTextView;
        TextView REPTextView;
        TextView XTextView;
        TextView FXTextView;

        ViewHolder(View itemView) {
            super(itemView);

            iterationTextView = itemView.findViewById(R.id.iteration);
            LEPTextView = itemView.findViewById(R.id.LEP);
            REPTextView = itemView.findViewById(R.id.REP);
            XTextView = itemView.findViewById(R.id.X);
            FXTextView = itemView.findViewById(R.id.FX);
        }
    }
}