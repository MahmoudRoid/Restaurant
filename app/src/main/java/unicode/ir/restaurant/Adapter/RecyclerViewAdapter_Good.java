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

import unicode.ir.restaurant.Database.db_goods;
import unicode.ir.restaurant.R;


public class RecyclerViewAdapter_Good extends RecyclerView.Adapter<RecyclerViewAdapter_Good.ViewHolder> {

    List<db_goods> ItemsList;
    Typeface San;
    Context mContext;


    public RecyclerViewAdapter_Good(List<db_goods> row, Typeface San, Context context) {
        this.ItemsList = row;
        this.San = San;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,description,price,discount;
        ImageView imageView;
        ElegantNumberButton picker ;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitle    = (TextView) itemView.findViewById(R.id.good_title);
            description    = (TextView) itemView.findViewById(R.id.good_description);
            price    = (TextView) itemView.findViewById(R.id.good_price_value);
            discount    = (TextView) itemView.findViewById(R.id.good_discount_value);
            imageView    = (ImageView) itemView.findViewById(R.id.good_imgCircleImage);
            picker = (ElegantNumberButton) itemView.findViewById(R.id.good_picker);


            txtTitle.setTypeface(San);
            description.setTypeface(San);
            price.setTypeface(San);
            discount.setTypeface(San);

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
                .inflate(R.layout.item_good, viewGroup, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder RowViewHolder, final int position) {
        RowViewHolder.picker.setRange(0,(int)ItemsList.get(position).getInventory());
        RowViewHolder.txtTitle.setText(ItemsList.get(position).getName());
        RowViewHolder.description.setText(ItemsList.get(position).getDescription());
        RowViewHolder.price.setText(String.valueOf(ItemsList.get(position).getSellprice()));
        RowViewHolder.discount.setText(String.valueOf(ItemsList.get(position).getDiscount()));
        RowViewHolder.picker.setNumber(String.valueOf(ItemsList.get(position).getGoodcount()));

        Glide.with(mContext).load(ItemsList.get(position).getImageurl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_launcher)
                .into(RowViewHolder.imageView);



        RowViewHolder.picker.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                // TODO :  update database
                db_goods db = Select.from(db_goods.class).where(Condition.prop("gid").eq(ItemsList.get(position).getGid())).first();
                db.goodcount = newValue;
                db.save();
            }
        });

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
