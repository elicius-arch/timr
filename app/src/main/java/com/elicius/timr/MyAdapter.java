package com.elicius.timr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Source: https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Timer> mDataset;
    private MainActivity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Timer> myDataset, MainActivity activity) {
        mDataset = myDataset;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Timer t = mDataset.get(position);
        holder.textView.setText(t.toString());
        holder.textView.setOnClickListener(new ViewClickListener(t));
        holder.textView.setOnLongClickListener(new ViewLongClickListener(t));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset == null)
            return 0;
        else
            return mDataset.size();
    }

    private class ViewClickListener implements View.OnClickListener {
        private Timer timer;

        public ViewClickListener(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, TimerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putInt(TimerActivity.SECONDS, timer.getSeconds());
            bundle.putInt(TimerActivity.MINUTES, timer.getMinutes());
            bundle.putInt(TimerActivity.HOURS, timer.getHours());
            intent.putExtra(TimerActivity.VALUES, bundle);
            activity.startActivity(intent);
        }

    }

    private class ViewLongClickListener implements View.OnLongClickListener {

        private Timer timer;

        public ViewLongClickListener(Timer timer) {
            this.timer = timer;
        }

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getString(R.string.delete))
                    .setMessage(activity.getString(R.string.delete_text, timer.toString()))
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteTimerFromList(timer);
                    dialog.dismiss();
                }
            })
            .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        private void deleteTimerFromList(Timer timer) {
            mDataset.remove(timer);
            MyAdapter.this.notifyDataSetChanged();
            SQLHandler sqlHandler = activity.getSQLHandler();
            sqlHandler.deleteTimer(timer);
        }
    }
}
