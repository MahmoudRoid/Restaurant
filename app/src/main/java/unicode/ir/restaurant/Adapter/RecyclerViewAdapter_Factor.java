package unicode.ir.restaurant.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import unicode.ir.restaurant.Classes.myApplication;
import unicode.ir.restaurant.Database.db_goods;
import unicode.ir.restaurant.Interface.IUpdatePrice;
import unicode.ir.restaurant.R;

public class RecyclerViewAdapter_Factor extends RecyclerView.Adapter<RecyclerViewAdapter_Factor.ViewHolder> {

    List<db_goods> ItemsList;
    Typeface San;
    Context mContext;
    IUpdatePrice delegate = null ;
    public myApplication application ;

    public RecyclerViewAdapter_Factor(List<db_goods> row, Typeface San, Context context, IUpdatePrice delegate) {
        this.ItemsList = row;
        this.San = San;
        this.mContext = context;
        this.delegate = delegate;
        application = (myApplication) context.getApplicationContext();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,price,totalprice;
        ImageView imageView;
        ElegantNumberButton picker ;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitle    = (TextView) itemView.findViewById(R.id.factor_title);
            totalprice    = (TextView) itemView.findViewById(R.id.factor_total_price_value);
            price    = (TextView) itemView.findViewById(R.id.factor_price_value);
            imageView    = (ImageView) itemView.findViewById(R.id.factor_imgCircleImage);
            picker = (ElegantNumberButton) itemView.findViewById(R.id.factor_picker);


            txtTitle.setTypeface(San);
            totalprice.setTypeface(San);
            price.setTypeface(San);


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
                .inflate(R.layout.item_factor, viewGroup, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder RowViewHolder, final int position) {
        RowViewHolder.picker.setNumber(String.valueOf(ItemsList.get(position).getGoodcount()));

         double totalPrice = Double.parseDouble(RowViewHolder.picker.getNumber()) *
                ItemsList.get(position).getSellprice();

        double discountValue = (ItemsList.get(position).getDiscount() * totalPrice)/100 ;
        totalPrice = totalPrice - discountValue;

        RowViewHolder.picker.setRange(0,(int)ItemsList.get(position).getInventory());
        RowViewHolder.txtTitle.setText(ItemsList.get(position).getName());
        RowViewHolder.price.setText(String.valueOf(ItemsList.get(position).getSellprice()));
        RowViewHolder.totalprice.setText(String.valueOf(totalPrice));


        Glide.with(mContext).load(ItemsList.get(position).getImageurl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher)
                .into(RowViewHolder.imageView);



        RowViewHolder.picker.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                //  update database
                db_goods db = Select.from(db_goods.class).where(Condition.prop("gid").eq(ItemsList.get(position).getGid())).first();
                db.goodcount = newValue;
                db.save();

                // calculate total price
                double item_totalPrice = Double.parseDouble(RowViewHolder.picker.getNumber()) *
                        ItemsList.get(position).getSellprice();
                RowViewHolder.totalprice.setText(String.valueOf(item_totalPrice));

                    delegate.updateTotalPrice();

            }
        });

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
