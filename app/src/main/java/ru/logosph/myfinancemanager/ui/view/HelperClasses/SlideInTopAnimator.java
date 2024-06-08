package ru.logosph.myfinancemanager.ui.view.HelperClasses;

import android.view.animation.DecelerateInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class SlideInTopAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationY(-holder.itemView.getHeight());
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(1000)
                .start();
        return true;
    }

//    @Override
//    public boolean animateAdd(RecyclerView.ViewHolder holder) {
//        holder.itemView.setAlpha(0f);
//        holder.itemView.animate()
//                .alpha(1f)
//                .setInterpolator(new DecelerateInterpolator())
//                .setDuration(700)
//                .start();
//        return true;
//    }
}
