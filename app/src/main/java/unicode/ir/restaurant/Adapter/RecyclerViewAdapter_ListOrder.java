package unicode.ir.restaurant.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import unicode.ir.restaurant.Database.db_order_list;
import unicode.ir.restaurant.Interface.IListOrder;
import unicode.ir.restaurant.R;


public class RecyclerViewAdapter_ListOrder extends RecyclerView.Adapter<RecyclerViewAdapter_ListOrder.ViewHolder> {

    List<db_order_list> ItemsList;
    Typeface San;
    Context mContext;
    IListOrder delegate = null;


    public RecyclerViewAdapter_ListOrder(List<db_order_list> row, Typeface San, Context context, IListOrder delegate) {
        this.ItemsList = row;
        this.San = San;
        this.mContext = context;
        this.delegate= delegate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate,txtPrice,txtContent,txtStatus;

        ViewHolder(View itemView) {
            super(itemView);
            txtDate    = (TextView) itemView.findViewById(R.id.order_date_value_txt);
            txtPrice    = (TextView) itemView.findViewById(R.id.order_price_value_txt);
            txtContent    = (TextView) itemView.findViewById(R.id.order_detail_txt);
            txtStatus    = (TextView) itemView.findViewById(R.id.order_status_txt);

            txtDate.setTypeface(San);
            txtPrice.setTypeface(San);
            txtContent.setTypeface(San);
            txtStatus.setTypeface(San);

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
                .inflate(R.layout.item_order, viewGroup, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder RowViewHolder, final int position) {
        // status
//        0 : submit sucssefully
//        1 : waiting for response
//        2 : closed
        String status = "";
        switch (ItemsList.get(position).getStatus()){
            case "0" :
                status = "سفارش شما تایید شد";
                break;
            case "1" :
                status = "سفارش شما در صف تایید میباشد";
                break;
            case "2" :
                status = "متاسفانه سفارش شما تایید نشد";
                break;
        }

        RowViewHolder.txtDate.setText(ItemsList.get(position).getOrderdate());
        RowViewHolder.txtPrice.setText(ItemsList.get(position).getPrice());
        RowViewHolder.txtContent.setText(String.valueOf(ItemsList.get(position).getOrderdetails()));
        RowViewHolder.txtStatus.setText(status);

        // add click listener for status
        RowViewHolder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.checkStatus(ItemsList.get(position).getOrderid());
            }
        });

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
