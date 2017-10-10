package com.dexter.customchatcell;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dexter.customchatcell.util.Config;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Point size=new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        Config.getInstance().setScreenHeight(size.y);
        Config.getInstance().setScreenWidth(size.x);

        ArrayList<MessageObject> chatMessages=new ArrayList<>();
        chatMessages.add(new MessageObject("Hi",MessageObject.SenderType.SELF));
        chatMessages.add(new MessageObject("Hello, How are you?",MessageObject.SenderType.OTHERS));
        chatMessages.add(new MessageObject("I am doing well. Where are you now?",MessageObject.SenderType.SELF));
        chatMessages.add(new MessageObject("I am in the cab. See you in 15 minutes",MessageObject.SenderType.OTHERS));
        chatMessages.add(new MessageObject("Ok, I am waiting outside my building. Please come asap.",MessageObject.SenderType.SELF));
        chatMessages.add(new MessageObject("Yes, I am coming soon",MessageObject.SenderType.OTHERS));
        chatMessages.add(new MessageObject("Please bring bring coffee from CCD while coming. CCD is on the way",MessageObject.SenderType.SELF));
        chatMessages.add(new MessageObject("Ok, anything else",MessageObject.SenderType.OTHERS));
        chatMessages.add(new MessageObject("nothing",MessageObject.SenderType.SELF));
        chatMessages.add(new MessageObject("Thank you.",MessageObject.SenderType.SELF));
        chatMessages.add(new MessageObject("Welcome",MessageObject.SenderType.OTHERS));

        RecyclerView recyclerView=new RecyclerView(this);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(this,chatMessages));

        LinearLayout LL =new LinearLayout(this);
        LL.setOrientation(LinearLayout.VERTICAL);
        LL.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LL.addView(recyclerView);

        setContentView(LL);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
        Context context;
        ArrayList<MessageObject> chatMessages;
        Adapter(Context context, ArrayList<MessageObject> chatMessages){
            this.context=context;
            this.chatMessages=chatMessages;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout LL=new LinearLayout(context);
            LL.setOrientation(LinearLayout.VERTICAL);
            LL.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(LL);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CustomCell cell=new CustomCell(context,chatMessages.get(position));
            if (chatMessages.get(position).getSender()==MessageObject.SenderType.SELF){
                holder.LL.setGravity(Gravity.RIGHT);
                holder.LL.setHorizontalGravity(Gravity.RIGHT);
            }else if (chatMessages.get(position).getSender()==MessageObject.SenderType.OTHERS){
                holder.LL.setGravity(Gravity.LEFT);
                holder.LL.setHorizontalGravity(Gravity.LEFT);
            }
            LinearLayout LL2=new LinearLayout(context);
            LL2.setOrientation(LinearLayout.VERTICAL);
            LL2.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LL2.addView(cell);
            holder.LL.addView(LL2);
        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            LinearLayout LL;
            public ViewHolder(View itemView) {
                super(itemView);
                LL = (LinearLayout) itemView;
                LL.setPadding(20,10,20,10);
            }
        }
    }
}
