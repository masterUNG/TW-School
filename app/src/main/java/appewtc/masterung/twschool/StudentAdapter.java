package appewtc.masterung.twschool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by masterUNG on 9/13/2016 AD.
 */
public class StudentAdapter extends BaseAdapter{

    //Explicit
    private Context context;
    private String[] nameStrings, surnameStrings;

    public StudentAdapter(Context context,
                          String[] nameStrings,
                          String[] surnameStrings) {
        this.context = context;
        this.nameStrings = nameStrings;
        this.surnameStrings = surnameStrings;
    }

    @Override
    public int getCount() {
        return nameStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.student_listview, viewGroup, false);

        //Bind Widget
        TextView nameTextView = (TextView) view1.findViewById(R.id.textView17);
        TextView surnameTextView = (TextView) view1.findViewById(R.id.textView18);

        //Show Text
        nameTextView.setText(nameStrings[i]);
        surnameTextView.setText(surnameStrings[i]);

        return view1;
    }
}   // Main Class
