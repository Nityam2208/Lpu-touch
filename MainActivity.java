package com.example.lputouch;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.text.InputType;

public class MainActivity extends Activity {
    LinearLayout root, content;
    int ORANGE=Color.rgb(255,138,101), DARK=Color.rgb(52,52,52), LIGHT=Color.rgb(255,249,236);
    String pin="";

    TextView tv(String s,int sp){ TextView t=new TextView(this); t.setText(s); t.setTextSize(sp); t.setTextColor(Color.DKGRAY); t.setPadding(14,10,14,10); return t; }
    GradientDrawable bg(int c,float r){ GradientDrawable g=new GradientDrawable(); g.setColor(c); g.setCornerRadius(r); return g; }

    @Override public void onCreate(Bundle b){ super.onCreate(b); showPin(); }

    void showPin(){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(Color.WHITE);
        root.setPadding(18,35,18,18);
        TextView title=tv("Enter your LPUTouch PIN",21); title.setTextColor(DARK);
        root.addView(title);
        TextView reset=tv("Reset Pin",13); reset.setTextColor(Color.WHITE); reset.setGravity(Gravity.CENTER);
        reset.setBackground(bg(Color.RED,20)); LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(90,45); rp.gravity=Gravity.RIGHT; rp.topMargin=-48; root.addView(reset,rp);
        TextView sub=tv("Please enter your secure PIN to access your application.",13); root.addView(sub);
        LinearLayout boxes=new LinearLayout(this); boxes.setGravity(Gravity.CENTER); boxes.setPadding(0,25,0,10);
        final EditText hidden=new EditText(this); hidden.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        hidden.setMaxLength(6); hidden.setSingleLine(); hidden.setBackgroundColor(Color.TRANSPARENT);
        root.addView(hidden,new LinearLayout.LayoutParams(1,1));
        TextView status=tv("○  ○  ○  ○  ○  ○",27); status.setGravity(Gravity.CENTER); root.addView(status);
        hidden.addTextChangedListener(new android.text.TextWatcher(){ public void beforeTextChanged(CharSequence s,int a,int c,int d){} public void onTextChanged(CharSequence s,int a,int b,int c){ pin=s.toString(); StringBuilder x=new StringBuilder(); for(int i=0;i<6;i++) x.append(i<s.length()?"●  ":"○  "); status.setText(x.toString()); if(s.length()==6){ showDashboard(); }} public void afterTextChanged(android.text.Editable e){} });
        Space sp=new Space(this); root.addView(sp,new LinearLayout.LayoutParams(1,0,1));
        GridLayout keys=new GridLayout(this); keys.setColumnCount(3);
        String[] nums={"1","2","3","4","5","6","7","8","9","⌫","0","✓"};
        for(String n:nums){ Button bt=new Button(this); bt.setText(n); bt.setTextSize(20); bt.setBackgroundColor(Color.TRANSPARENT); bt.setOnClickListener(v->{ if(n.equals("⌫")) { if(pin.length()>0) hidden.setText(pin.substring(0,pin.length()-1)); } else if(n.equals("✓")) { if(pin.length()==6) showDashboard(); } else if(pin.length()<6) hidden.append(n); }); keys.addView(bt,new ViewGroup.LayoutParams(120,75)); }
        root.addView(keys);
        setContentView(root);
    }

    void showDashboard(){
        root.removeAllViews(); root.setPadding(0,0,0,0); root.setBackgroundColor(Color.WHITE);
        LinearLayout header=new LinearLayout(this); header.setGravity(Gravity.CENTER_VERTICAL); header.setPadding(10,10,10,10);
        TextView menu=tv("☰",28); header.addView(menu,new LinearLayout.LayoutParams(55,65));
        TextView h=tv("Dashboard",20); h.setGravity(Gravity.CENTER); header.addView(h,new LinearLayout.LayoutParams(0,65,1));
        TextView bell=tv("♧",25); bell.setGravity(Gravity.CENTER); header.addView(bell,new LinearLayout.LayoutParams(55,65)); root.addView(header);
        menu.setOnClickListener(v->showDrawer());

        ScrollView sv=new ScrollView(this); content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(12,4,12,10);
        TextView tt=tv("Today's Timetable                         Your Dost",18); content.addView(tt);
        TextView none=tv("No TimeTable Available",17); none.setGravity(Gravity.CENTER); none.setPadding(0,30,0,30); none.setBackground(bg(0xFFFAFAFA,12)); content.addView(none);
        TextView add=tv("Add More Tiles                                      +",18); content.addView(add);
        GridLayout grid=new GridLayout(this); grid.setColumnCount(3);
        String[] tiles={"📣\nAnnounce","🎓\nEdu\nRevolution","💳\nFee\nStatement","📋\nAttendance","📝\nAssignment","📊\nResults","🧾\nExams","📅\nRMS Status","👥\nEvents"};
        for(String s:tiles){ TextView x=tv(s,14); x.setGravity(Gravity.CENTER); x.setBackground(bg(LIGHT,18)); x.setPadding(4,18,4,18); GridLayout.LayoutParams gp=new GridLayout.LayoutParams(); gp.width=0; gp.height=135; gp.columnSpec=GridLayout.spec(GridLayout.UNDEFINED,1f); gp.setMargins(5,5,5,5); grid.addView(x,gp);
            if(s.contains("RMS")) x.setOnClickListener(v->showSimple("RMS Status","Current semester RMS status\n\nStatus: Active\nData: Demo only"));
            if(s.contains("Attendance")) x.setOnClickListener(v->showSimple("Attendance","Overall Attendance: 81%\n\nCSE202: 86%\nCSE205: 78%\nCSE306: 82%\nMTH401: 80%"));
            if(s.contains("Results")) x.setOnClickListener(v->showSimple("Results","Semester Result (Demo)\n\nCSE202  •  A\nCSE205  •  A+\nCSE306  •  B+\nMTH401  •  A"));
            if(s.contains("Exams")) x.setOnClickListener(v->showSimple("Exams","Upcoming Exams (Demo)\n\nCSE202  •  12 May\nCSE205  •  15 May"));
            if(s.contains("Assignment")) x.setOnClickListener(v->showSimple("Assignments","4 pending assignments\n\nC++ Lab • Due Friday\nDBMS • Due Monday"));
            if(s.contains("Fee")) x.setOnClickListener(v->showSimple("Fee Statement","Fee Statement (Demo)\n\nTotal: ₹1,20,000\nPaid: ₹90,000\nDue: ₹30,000"));
            if(s.contains("Events")) x.setOnClickListener(v->showSimple("Events","Campus Events (Demo)\n\nTech Fest\nSports Meet\nCultural Night"));
        }
        content.addView(grid);
        sv.addView(content); root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav=new LinearLayout(this); nav.setGravity(Gravity.CENTER); String[] ns={"▦\nDashboard","▤\nHappenings","☑\nRMS","▥\nGuest Room"}; for(String s:ns){ TextView n=tv(s,12); n.setGravity(Gravity.CENTER); nav.addView(n,new LinearLayout.LayoutParams(0,70,1)); } root.addView(nav);
        // Make timetable clickable from the top card
        none.setOnClickListener(v->showTimetable());
        tt.setOnClickListener(v->showTimetable());
    }

    void showDrawer(){
        final Dialog d=new Dialog(this); LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(16,20,16,10);
        TextView profile=tv("👤\n\nNityam Bajaj\n12512759\nP132:B.Tech. (Computer Science and Engineering)(2025)",16); profile.setGravity(Gravity.CENTER); profile.setTextColor(Color.WHITE); profile.setBackground(bg(ORANGE,0)); l.addView(profile,new LinearLayout.LayoutParams(-1,220));
        EditText search=new EditText(this); search.setHint("Search"); l.addView(search);
        String[] items={"Attendance","Placement Barcode Scanner","Book Appointment","Transport Preference","Change UMS Password","View Marks","Event Attendance","Doctor Appointment","Document Upload","Placement Drive","Elective Polling"};
        for(String s:items){ TextView x=tv("  "+s,15); l.addView(x); }
        Button out=new Button(this); out.setText("LOGOUT"); out.setTextColor(Color.DKGRAY); l.addView(out); out.setOnClickListener(v->{d.dismiss();showPin();});
        d.setContentView(l); Window w=d.getWindow(); if(w!=null) w.setLayout((int)(getResources().getDisplayMetrics().widthPixels*.78),-1); d.show(); if(w!=null) w.setLayout((int)(getResources().getDisplayMetrics().widthPixels*.78),-1);
    }

    void showTimetable(){
        root.removeAllViews(); LinearLayout top=new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL); TextView back=tv("‹",32); TextView title=tv("TimeTable",20); title.setGravity(Gravity.CENTER); top.addView(back,new LinearLayout.LayoutParams(55,65)); top.addView(title,new LinearLayout.LayoutParams(0,65,1)); root.addView(top); back.setOnClickListener(v->showDashboard());
        LinearLayout days=new LinearLayout(this); days.setPadding(5,5,5,5); String[] ds={"Monday","Tuesday","Wednesday","Thursday","Friday"}; for(String day:ds){Button b=new Button(this); b.setText(day); days.addView(b,new LinearLayout.LayoutParams(0,55,1)); b.setOnClickListener(v->renderDay(day));} root.addView(days);
        renderDay("Monday");
    }
    void renderDay(String day){
        if(root.getChildCount()>2) root.removeViewAt(2);
        LinearLayout list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); list.setPadding(12,8,12,8); TextView head=tv(day,21); head.setGravity(Gravity.CENTER); list.addView(head);
        String[][] data={
            {"09:20-10:10 AM","Practical / G:1","C:CSE205 / R: 38-914 / S:K4P25HW"},
            {"10:10-11:00 AM","Practical / G:1","C:CSE205 / R: 38-914 / S:K4P25HW"},
            {"11:00-11:50 AM","Practical / G:0","C:CSE423 / R: 38-716 / S:K4P25HW"},
            {"12:40-01:30 PM","Practical / G:0","C:CSE306 / R: 26-404 / S:K4P25HW"},
            {"01:30-02:20 PM","Practical / G:0","C:CSE306 / R: 26-404 / S:K4P25HW"},
            {"02:20-03:10 PM","Lecture / G:All","C:MTH401 / R: 26-404 / S:K4P25HW"},
            {"03:10-04:00 PM","Lecture / G:All","C:CSE202 / R: 26-404 / S:K4P25HW"},
            {"04:00-04:50 PM","Lecture / G:All","C:CSE202 / R: 26-404 / S:K4P25HW"}};
        for(String[] a:data){ TextView c=tv(a[0]+"\n"+a[1]+"\n"+a[2],13); c.setGravity(Gravity.CENTER); c.setPadding(8,12,8,12); c.setBackground(bg(0xFFF7F7F7,8)); list.addView(c,new LinearLayout.LayoutParams(-1,88)); }
        ScrollView s=new ScrollView(this); s.addView(list); root.addView(s,new LinearLayout.LayoutParams(-1,0,1));
    }

    void showSimple(String title,String body){
        root.removeAllViews(); LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); TextView h=tv("‹   "+title,21); l.addView(h); h.setOnClickListener(v->showDashboard()); TextView b=tv(body,17); b.setPadding(22,30,22,10); l.addView(b); root.addView(l);
    }
}