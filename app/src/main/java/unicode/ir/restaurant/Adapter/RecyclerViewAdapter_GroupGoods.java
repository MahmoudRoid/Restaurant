package unicode.ir.restaurant.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import unicode.ir.restaurant.Objects.Object_GoodGroup;
import unicode.ir.restaurant.R;

/**
 * Created by soheil syetem on 12/25/2016.
 */

public class RecyclerViewAdapter_GroupGoods extends RecyclerView.Adapter<RecyclerViewAdapter_GroupGoods.ViewHolder> {

    List<Object_GoodGroup> ItemsList;
    Typeface San;
    Context mContext;

    public RecyclerViewAdapter_GroupGoods(List<Object_GoodGroup> row, Typeface San, Context context) {
        this.ItemsList = row;
        this.San = San;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitle    = (TextView) itemView.findViewById(R.id.txtTitle);


            txtTitle.setTypeface(San);

        }// Cunstrator()

    }// end class ViewHolder{}


    @Override
    public int getItemCount() {
        return ItemsList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_group_good, viewGroup, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder RowViewHolder, final int position) {
        RowViewHolder.txtTitle.setText(ItemsList.get(position).getName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
