package nm.droid.com.numericalmethods.integration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nm.droid.com.numericalmethods.R;

public class IntegrationAdapter extends RecyclerView.Adapter<IntegrationAdapter.ViewHolder> {

    List<IntegrationRow> items;
    Context context;

    IntegrationAdapter(Context context, List<IntegrationRow> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public IntegrationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_integration, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IntegrationAdapter.ViewHolder holder, int position) {
        IntegrationRow integrationRow = items.get(position);

        holder.xITextView.setText(integrationRow.mXI);
        holder.yITextView.setText(integrationRow.mYI);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView xITextView;
        TextView yITextView;

        ViewHolder(View itemView) {
            super(itemView);

            xITextView = itemView.findViewById(R.id.value_xi);
            yITextView = itemView.findViewById(R.id.value_yi);
        }
    }
}
