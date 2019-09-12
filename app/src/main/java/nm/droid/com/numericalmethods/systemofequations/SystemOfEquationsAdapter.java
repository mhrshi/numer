package nm.droid.com.numericalmethods.systemofequations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class SystemOfEquationsAdapter extends RecyclerView.Adapter<SystemOfEquationsAdapter.ViewHolder> {

    private List<SystemOfEquationsRow> items;
    private Context context;

    SystemOfEquationsAdapter(Context context, List<SystemOfEquationsRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public SystemOfEquationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_system_of_equations, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SystemOfEquationsAdapter.ViewHolder holder, int position) {
        SystemOfEquationsRow systemOfEquationsRow = items.get(position);

        holder.iterationTextView.setText(systemOfEquationsRow.mIteration);
        holder.XTextView.setText(systemOfEquationsRow.mX);
        holder.YTextView.setText(systemOfEquationsRow.mY);
        holder.ZTextView.setText(systemOfEquationsRow.mZ);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView iterationTextView;
        TextView XTextView;
        TextView YTextView;
        TextView ZTextView;

        ViewHolder(View itemView) {
            super(itemView);

            iterationTextView = itemView.findViewById(R.id.iteration);
            XTextView = itemView.findViewById(R.id.X);
            YTextView = itemView.findViewById(R.id.Y);
            ZTextView = itemView.findViewById(R.id.Z);
        }
    }
}
