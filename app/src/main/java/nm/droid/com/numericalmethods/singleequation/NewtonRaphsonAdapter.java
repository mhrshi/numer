package nm.droid.com.numericalmethods.singleequation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class NewtonRaphsonAdapter extends RecyclerView.Adapter<NewtonRaphsonAdapter.ViewHolder> {

    private List<NewtonRaphsonRow> items;
    private Context context;

    NewtonRaphsonAdapter(Context context, List<NewtonRaphsonRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public NewtonRaphsonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_newton_raphson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewtonRaphsonAdapter.ViewHolder holder, int position) {
        NewtonRaphsonRow newtonRaphsonRow = items.get(position);

        holder.iterationTextView.setText(newtonRaphsonRow.mIteration);
        holder.XTextView.setText(newtonRaphsonRow.mX);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView iterationTextView;
        TextView XTextView;

        ViewHolder(View itemView) {
            super(itemView);

            iterationTextView = itemView.findViewById(R.id.iteration);
            XTextView = itemView.findViewById(R.id.X);
        }
    }
}
