package nm.droid.com.numericalmethods;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MethodAdapter extends RecyclerView.Adapter<MethodAdapter.ViewHolder> {

    private List<MethodRow> items;
    private Context context;
    private MethodListActivity mMethodListActivity;

    MethodAdapter(Context context, List<MethodRow> items, MethodListActivity methodListActivity) {
        this.context = context;
        this.items = items;
        mMethodListActivity = methodListActivity;
    }

    @Override
    public MethodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_method, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MethodAdapter.ViewHolder holder, final int position) {
        MethodRow methodRow = items.get(position);

        holder.primaryTextView.setText(methodRow.mPrimaryText);
        holder.subTextView.setText(methodRow.mSubText);
        holder.supportingTextView.setText(methodRow.mSupportingText);
        holder.methodCardView.setOnClickListener(v -> mMethodListActivity.runCorrespondingMethod(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView methodCardView;
        TextView primaryTextView;
        TextView subTextView;
        TextView supportingTextView;

        ViewHolder(View itemView) {
            super(itemView);

            methodCardView = itemView.findViewById(R.id.card_method);
            primaryTextView = itemView.findViewById(R.id.primary_text);
            subTextView = itemView.findViewById(R.id.sub_text);
            supportingTextView = itemView.findViewById(R.id.supporting_text);
        }
    }
}
