package timothypaetz.com.recyclersectionheader;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by paetztm on 2/6/2017.
 */

public class RecyclerSectionItemDecorationStyle extends RecyclerView.ItemDecoration {

    private final int             headerOffset;
    private final boolean         sticky;
    private final SectionCallback sectionCallback;



    private View     headerView1;
    private View     headerView2;
    private TextView header;
    private TextView header2;

    public RecyclerSectionItemDecorationStyle(int headerHeight, boolean sticky, @NonNull SectionCallback sectionCallback) {
        headerOffset = headerHeight;
        this.sticky = sticky;
        this.sectionCallback = sectionCallback;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect,
                             view,
                             parent,
                             state);

        int pos = parent.getChildAdapterPosition(view);
        if (sectionCallback.isSection(pos)) {
            outRect.top = headerOffset;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c,
                         parent,
                         state);

        if (headerView1 == null) {
            headerView1 = inflateHeaderView1(parent);
            header = (TextView) headerView1.findViewById(R.id.list_item_section_text);
            fixLayoutSize(headerView1,
                          parent);
        }

        if (headerView2 == null) {
            headerView2 = inflateHeaderView2(parent);
            header2 = (TextView) headerView2.findViewById(R.id.list_item_section_text);
            fixLayoutSize(headerView2,
                          parent);
        }

        CharSequence previousHeader = "";
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);

            CharSequence title = sectionCallback.getSectionHeader(position);


            if (!previousHeader.equals(title) || sectionCallback.isSection(position)) {
                if (sectionCallback.getSectionType(position).equals("B")){
                    header2.setText(title);
                    drawHeader(c,
                            child,
                            headerView2);
                    previousHeader = title;
                }else{
                    header.setText(title);
                    drawHeader(c,
                            child,
                            headerView1);
                    previousHeader = title;
                }

            }
        }
    }

    private void drawHeader(Canvas c, View child, View headerView) {
        c.save();
        if (sticky) {
            c.translate(0,
                        Math.max(0,
                                 child.getTop() - headerView.getHeight()));
        } else {
            c.translate(0,
                        child.getTop() - headerView.getHeight());
        }
        headerView.draw(c);
        c.restore();
    }

    private View inflateHeaderView1(RecyclerView parent) {
        return LayoutInflater.from(parent.getContext())
                             .inflate(R.layout.recycler_section_header,
                                      parent,
                                      false);
    }

    private View inflateHeaderView2(RecyclerView parent) {
        return LayoutInflater.from(parent.getContext())
                             .inflate(R.layout.recycler_section_header2,
                                      parent,
                                      false);
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private void fixLayoutSize(View view, ViewGroup parent) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(),
                                                         View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(),
                                                          View.MeasureSpec.UNSPECIFIED);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                                                       parent.getPaddingLeft() + parent.getPaddingRight(),
                                                       view.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                                                        parent.getPaddingTop() + parent.getPaddingBottom(),
                                                        view.getLayoutParams().height);

        view.measure(childWidth,
                     childHeight);

        view.layout(0,
                    0,
                    view.getMeasuredWidth(),
                    view.getMeasuredHeight());
    }

    public interface SectionCallback {

        boolean isSection(int position);

        CharSequence getSectionHeader(int position);

        CharSequence getSectionType(int position);
    }
}


